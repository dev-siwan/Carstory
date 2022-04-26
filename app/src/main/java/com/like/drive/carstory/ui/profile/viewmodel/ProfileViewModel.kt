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
import com.like.drive.carstory.ui.base.ext.isNickName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userInfo: UserInfo,
    private val userRepository: UserRepository
) :
    BaseViewModel() {

    val nickObserver = ObservableField<String>()
    val introObserver = ObservableField<String>()
    val imageClickEvent = SingleLiveEvent<Unit>()

    val nickValidEvent = SingleLiveEvent<Unit>()

    val isLoading = SingleLiveEvent<Boolean>()
    val errorEvent = SingleLiveEvent<Unit>()
    val signOut = SingleLiveEvent<Unit>()
    val completeEvent = SingleLiveEvent<ProfileStatus>()
    val existNicknameEvent = SingleLiveEvent<Unit>()

    var profileStatus: ProfileStatus? = null

    private val _isFirstProfile = MutableLiveData(false)
    val isFirstProfile: LiveData<Boolean> get() = _isFirstProfile

    private var imgFile: File? = null

    init {
        userInfo.userInfo?.let {

            if (it.nickName == null) {
                profileStatus = ProfileStatus.INIT
                _isFirstProfile.value = true
                return@let
            }

            setData(it)

        }
    }

    fun setImageFile(file: File?) {
        imgFile = file
    }

    fun updateProfile() {

        val nickName = nickObserver.get()!!
        val intro = introObserver.get()

        if (!nickName.isNickName() || nickName.contains(" ")) {
            nickValidEvent.call()
            return
        }

        isLoading.value = true
        viewModelScope.launch {
            userRepository.updateProfile(nickName = nickName,
                intro = intro,
                success = {
                    complete(nickName, intro)
                },
                fail = {
                    viewModelScope.launch {
                        errorEvent.call()
                    }
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
    }

    fun signOut() {
        userInfo.signOut()
        signOut.call()
    }

    private fun complete(nickName: String, intro: String?) {
        userInfo.updateProfile(nickName, intro)
        completeEvent.value = profileStatus
        isLoading.value = false
    }

    enum class ProfileStatus {
        INIT, MODIFY
    }

}