package com.like.drive.motorfeed.ui.notice.detail.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.repository.notice.NoticeRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class NoticeDetailViewModel(private val repository: NoticeRepository) : BaseViewModel(),
    KoinComponent {

    private val _noticeData = MutableLiveData<NoticeData>()
    val noticeData: LiveData<NoticeData> get() = _noticeData

    val errorEvent = SingleLiveEvent<@StringRes Int>()

    fun getNotice(nid: String) {
        viewModelScope.launch {
            repository.getNotice(nid,
                success = {
                    _noticeData.value = it
                },
                fail = { errorEvent.value = R.string.notice_error_message })
        }
    }
}