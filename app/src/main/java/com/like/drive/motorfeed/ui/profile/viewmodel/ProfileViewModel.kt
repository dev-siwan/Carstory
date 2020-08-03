package com.like.drive.motorfeed.ui.profile.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(val userRepository: UserRepository) : BaseViewModel() {

    val nickName = ObservableField<String>()
    val intro = ObservableField<String>()
    val imgUrl = ObservableField<String>()

    val imageClickEvent = SingleLiveEvent<Unit>()

    val isLoading = SingleLiveEvent<Boolean>()

    private var imgFile: File? = null


    fun setImageFile(file: File) {
        imgFile = file
        imgUrl.set(file.path)
    }

    fun updateProfile() {
        isLoading.value = true
        viewModelScope.launch {
            userRepository.updateProfile(nickName = nickName.get()!!,
                intro = intro.get(),
                imgFile = imgFile,
                success = {
                    complete(nickName.get()!!, intro.get(), it?.toString())
                },
                fail = {},
                empty = {})
        }
    }


    private fun complete(nickNameValue: String, introValue: String?, imgUrlValue: String?) {
        UserInfo.userInfo?.apply {
            nickName = nickNameValue
            intro = introValue
            profileImgUrl = imgUrlValue
        }
        isLoading.value = false
    }



}