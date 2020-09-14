package com.like.drive.motorfeed.ui.notification.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.data.notification.NotificationSendData
import com.like.drive.motorfeed.repository.notification.NotificationRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class NotificationViewModel(val repository: NotificationRepository) : BaseViewModel() {

    private val _notificationList = MutableLiveData<List<NotificationSendData>>()
    val notificationList: LiveData<List<NotificationSendData>> get() = _notificationList

    fun init() {
        viewModelScope.launch {
            _notificationList.value = repository.getList()
        }
    }
}