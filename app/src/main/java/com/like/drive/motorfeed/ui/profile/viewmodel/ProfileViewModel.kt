package com.like.drive.motorfeed.ui.profile.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
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
    val completeEvent = SingleLiveEvent<Unit>()

    private var imgFile: File? = null


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
                    complete(nickObserver.get()!!, introObserver.get(), it?.toString())
                },
                fail = {
                    errorEvent.call()
                },
                notUser = {
                    signOut()
                })
        }
    }


    fun signOut(){
        UserInfo.signOut()
        signOut.call()
    }



    private fun complete(nickName: String, intro: String?, imgUrl: String?) {
        UserInfo.userInfo?.let {
            it.nickName = nickName
            it.intro = intro
            it.profileImgUrl = imgUrl
        }
        completeEvent.call()
        isLoading.value = false
    }

}