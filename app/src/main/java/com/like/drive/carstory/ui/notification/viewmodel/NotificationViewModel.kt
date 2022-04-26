package com.like.drive.carstory.ui.notification.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.repository.notification.NotificationRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NotificationViewModel @Inject constructor(val repository: NotificationRepository) :
    BaseViewModel() {

    private val _notificationList = MutableLiveData<List<NotificationSendData>>()
    val notificationList: LiveData<List<NotificationSendData>> get() = _notificationList

    val clickItemEvent = SingleLiveEvent<NotificationSendData>()

    val isEmptyObservable = ObservableBoolean(false)

    val removeItemEvent = SingleLiveEvent<Long>()

    fun init() {
        viewModelScope.launch {
            repository.getList().let {
                if (it.isEmpty()) {
                    isEmptyObservable.set(true)
                    return@launch
                }

                _notificationList.value =
                    it.map { data -> NotificationSendData().entityToData(data) }
                isEmptyObservable.set(false)
            }
        }
    }

    fun onClickListener(data: NotificationSendData) {
        clickItemEvent.value = data
    }

    fun removeListener(id: Long?) {
        id?.let {
            removeItemEvent.value = it
        }
    }

    fun removeNotificationItem(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteItem(id)
            }
            init()
        }
    }
}