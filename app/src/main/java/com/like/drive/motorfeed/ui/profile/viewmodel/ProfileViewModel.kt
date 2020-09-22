package com.like.drive.motorfeed.ui.profile.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
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

    lateinit var profileStatus: ProfileStatus

    val isInit = ObservableBoolean(false)

    private var imgFile: File? = null

    init {
        UserInfo.userInfo?.let {

            if (it.nickName == null) {
                profileStatus = ProfileStatus.INIT
                isInit.set(true)
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