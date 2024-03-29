package com.like.drive.carstory.ui.notice.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.notice.NoticeData
import com.like.drive.carstory.repository.notice.NoticeRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.common.data.LoadingStatus
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class NoticeListViewModel(private val noticeRepository: NoticeRepository) : BaseViewModel() {

    private val _noticeList = MutableLiveData<List<NoticeData>>()
    val noticeList: LiveData<List<NoticeData>> get() = _noticeList

    var loadingStatus: LoadingStatus? = null

    val isRefresh = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isMore = ObservableBoolean(false)

    var isFirst = true
    private var lastDate: Date? = null

    val clickNoticeDataEvent = SingleLiveEvent<NoticeData>()
    val clickMenuEvent = SingleLiveEvent<NoticeData>()

    val title = ObservableField<String>()
    val message = ObservableField<String>()
    val mdFileName = ObservableField<String>()

    val addCompleteEvent = SingleLiveEvent<NoticeData>()
    val removeCompleteEvent = SingleLiveEvent<NoticeData>()
    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val loadingEvent = SingleLiveEvent<Boolean>()

    var noticeData: NoticeData? = null

    fun initData() {
        isFirst = true
        lastDate = null

        loadingStatus()

        getList()
    }

    fun moreData(date: Date) {

        isFirst = false
        lastDate = date

        loadingStatus = LoadingStatus.MORE
        loadingStatus()

        getList(date)
    }

    private fun getList(date: Date? = null) {
        viewModelScope.launch {
            noticeRepository.getList(date ?: Date())
                .catch {

                    loadingStatus()
                }
                .collect {
                    _noticeList.value = it
                    loadingStatus()
                }
        }
    }

    private fun loadingStatus() {
        when (loadingStatus) {
            LoadingStatus.INIT -> {
                if (isLoading.get()) isLoading.set(false) else isLoading.set(true)
            }
            LoadingStatus.REFRESH -> {
                if (isRefresh.get()) isRefresh.set(false) else isRefresh.set(true)
            }
            else -> {
                if (isMore.get()) isMore.set(false) else isMore.set(true)
            }
        }
    }

    fun getLastDate() = _noticeList.value?.lastOrNull()?.createDate == lastDate

    fun initNoticeData(noticeData: NoticeData?) {
        this.noticeData = noticeData

        title.set(noticeData?.title)
        message.set(noticeData?.message)
        mdFileName.set(noticeData?.mdFile)
    }

    fun uploadNotice(title: String, message: String, mdFile: String) {

        val noticeData = this.noticeData?.let {

            NoticeData().updateNoticeData(it, title, message, mdFile)

        } ?: NoticeData().addNoticeData(title, message, mdFile)

        setNotice(noticeData)
    }

    private fun setNotice(noticeData: NoticeData) {

        loadingEvent.value = true

        viewModelScope.launch {
            noticeRepository.setNotice(noticeData,
                success = {
                    addCompleteEvent.value = noticeData
                    loadingEvent.value = false
                },
                fail = {
                    errorEvent.value = R.string.notice_upload_fail_message
                    loadingEvent.value = true
                })
        }
    }

    fun removeNotice(noticeData: NoticeData) {

        loadingEvent.value = true

        viewModelScope.launch {
            noticeRepository.removeNotice(noticeData,
                success = {
                    removeCompleteEvent.value = it
                    loadingEvent.value = false
                },
                fail = {
                    errorEvent.value = R.string.notice_remove_fail_message
                    loadingEvent.value = true
                })
        }
    }

    fun sendNotification(noticeData: NoticeData) {
        viewModelScope.launch {
            noticeRepository.sendNotification(noticeData)
        }
    }

    fun setOnNoticeClickListener(noticeData: NoticeData) {
        clickNoticeDataEvent.value = noticeData
    }

    fun setOnMenuClickListener(noticeData: NoticeData) {
        clickMenuEvent.value = noticeData
    }

}