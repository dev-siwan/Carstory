package com.like.drive.carstory.ui.notification.viewmodel

import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.ui.base.BaseViewModel

class NotificationSettingViewModel : BaseViewModel() {

    val checkNotice: (Boolean) -> Unit = this::setNoticeSubscribe
    private fun setNoticeSubscribe(isSubscribe: Boolean) {
        UserInfo.updateNoticeSubScribe(isSubscribe)
    }

    val checkComment: (Boolean) -> Unit = this::setCommentSubscribe
    private fun setCommentSubscribe(isSubscribe: Boolean) {
        UserInfo.updateCommentSubScribe(isSubscribe)
    }

}