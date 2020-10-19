package com.like.drive.carstory.ui.main.viewmodel

import android.view.View
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val uploadClickEvent = SingleLiveEvent<Unit>()
    val userMessageEvent = SingleLiveEvent<String>()

    init {
        userMessage()
    }

    private fun userMessage() {
        if (UserInfo.userInfo?.userMessageStatus == true) {
            UserInfo.userInfo?.userMessage?.let {
                if (!it.isBlank()) {
                    userMessageEvent.value = it
                }
            }
        }
    }

    fun confirmUserMessage() {
        UserInfo.userInfo?.uid?.let {
            viewModelScope.launch {
                userRepository.confirmUserMessage(it)
                UserInfo.userInfo?.userMessageStatus = false
            }
        }
    }

}