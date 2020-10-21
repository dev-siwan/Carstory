package com.like.drive.carstory.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.pref.UserPref
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel(private val userRepository: UserRepository) : BaseViewModel(), KoinComponent {

    private val userPref: UserPref by inject()

    val showPermissionEvent = SingleLiveEvent<Unit>()

    val uploadClickEvent = SingleLiveEvent<Unit>()
    val userMessageEvent = SingleLiveEvent<String>()

    val notificationRefreshEvent = SingleLiveEvent<Unit>()

    init {
        checkPermission()
        userMessage()
    }

    private fun checkPermission() {
        if (!userPref.isPermissionPopUp) {
            showPermissionEvent.call()
        }
    }

    fun confirmPermission() {
        userPref.isPermissionPopUp = true
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

    fun onNotificationRefreshListener() {
        notificationRefreshEvent.call()
    }

}