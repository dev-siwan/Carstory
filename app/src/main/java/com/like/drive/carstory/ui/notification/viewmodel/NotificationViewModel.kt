package com.like.drive.carstory.ui.notification.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.repository.notification.NotificationRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class NotificationViewModel(val repository: NotificationRepository) : BaseViewModel() {

    private val _notificationList = MutableLiveData<List<NotificationSendData>>()
    val notificationList: LiveData<List<NotificationSendData>> get() = _notificationList

    val clickItemEvent = SingleLiveEvent<NotificationSendData>()

    val isEmptyObservable = ObservableBoolean(false)

    val removeItemEvent = SingleLiveEvent<Int>()

    fun init() {
        viewModelScope.launch {
            repository.getList().distinctUntilChanged().collect {
                if (it.isEmpty()) {
                    isEmptyObservable.set(true)
                    return@collect
                }
                _notificationList.value =
                    it.map { data -> NotificationSendData().entityToData(data) }
            }
        }
    }

    fun onClickListener(data: NotificationSendData) {
        clickItemEvent.value = data
    }

    fun removeListener(id: Int?) {
        id?.let {
            removeItemEvent.value = it
        }
    }

    fun removeNotificationItem(id: Int) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }
}