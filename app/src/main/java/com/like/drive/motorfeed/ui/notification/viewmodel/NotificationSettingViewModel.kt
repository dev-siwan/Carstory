package com.like.drive.motorfeed.ui.notification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.ui.base.BaseViewModel

class NotificationSettingViewModel : BaseViewModel() {

    private val _noticeCheck = MutableLiveData<Boolean>()
    val noticeCheck: LiveData<Boolean> get() = _noticeCheck

    private val _commentCheck = MutableLiveData<Boolean>()
    val commentCheck: LiveData<Boolean> get() = _commentCheck

    init {
        UserInfo.run {
            _noticeCheck.value = isNoticeTopic ?: true
            _commentCheck.value = userInfo?.isCommentSubscribe ?: true
        }

    }

    fun setNoticeSubscribe(isSubscribe: Boolean) {
        UserInfo.updateNoticeSubScribe(isSubscribe)
    }

    fun setCommentSubscribe(isSubscribe: Boolean) {
        UserInfo.updateCommentSubScribe(isSubscribe)
    }

}