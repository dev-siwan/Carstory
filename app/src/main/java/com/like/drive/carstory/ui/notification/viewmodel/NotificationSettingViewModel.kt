package com.like.drive.carstory.ui.notification.viewmodel

import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationSettingViewModel @Inject constructor(private val userInfo: UserInfo) :
    BaseViewModel() {

    val checkNotice: (Boolean) -> Unit = this::setNoticeSubscribe
    private fun setNoticeSubscribe(isSubscribe: Boolean) {
        userInfo.updateNoticeSubScribe(isSubscribe)
    }

    val checkComment: (Boolean) -> Unit = this::setCommentSubscribe
    private fun setCommentSubscribe(isSubscribe: Boolean) {
        userInfo.updateCommentSubScribe(isSubscribe)
    }

}