package com.like.drive.carstory.ui.profile.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val nickObserver = ObservableField<String>()
    val introObserver = ObservableField<String>()
    val imgUrlObserver = ObservableField<String>()

    val imageClickEvent = SingleLiveEvent<Unit>()

    val isLoading = SingleLiveEvent<Boolean>()
    val errorEvent = SingleLiveEvent<Unit>()
    val signOut = SingleLiveEvent<Unit>()
    val completeEvent = SingleLiveEvent<ProfileStatus>()
    val existNicknameEvent = SingleLiveEvent<Unit>()

    var profileStatus: ProfileStatus?=null

    private val _isFirstProfile = MutableLiveData<Boolean>(false)
    val isFirstProfile: LiveData<Boolean> get() = _isFirstProfile

    private var imgFile: File? = null

    init {
        UserInfo.userInfo?.let {

            if (it.nickName == null) {
                profileStatus = ProfileStatus.INIT
                _isFirstProfile.value = true
                return@let
            }

            setData(it)

        }
    }

    fun setImageFile(file: File) {
        imgFile = file
        imgUrlObserver.set(file.path)
    }

    fun updateProfile() {
        isLoading.value = true
        viewModelScope.launch {
            userRepository.updateProfile(nickName = nickObserver.get()!!,
                intro = introObserver.get(),
                imgFile = imgFile,
                success = {
                    complete(nickObserver.get()!!, introObserver.get(), it)
                },
                fail = {
                    errorEvent.call()
                },
                notUser = {
                    signOut()
                    isLoading.value = false
                },
                existNickName = {
                    existNicknameEvent.call()
                    isLoading.value = false
                })
        }
    }

    fun setData(userData: UserData) {

        profileStatus = ProfileStatus.MODIFY

        nickObserver.set(userData.nickName)
        introObserver.set(userData.intro)
        imgUrlObserver.set(userData.profileImgPath)
    }

    fun signOut() {
        UserInfo.signOut()
        signOut.call()
    }

    private fun complete(nickName: String, intro: String?, imgPath: String?) {
        UserInfo.updateProfile(nickName, intro, imgPath)
        completeEvent.value = profileStatus
        isLoading.value = false
    }

    enum class ProfileStatus {
        INIT, MODIFY
    }

}