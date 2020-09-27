package com.like.drive.motorfeed.ui.notice.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.repository.notice.NoticeRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
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
    val initEmpty = ObservableBoolean(false)

    var isFirst = true
    var lastDate: Date? = null

    val clickNoticeDataEvent = SingleLiveEvent<NoticeData>()
    val clickMenuEvent = SingleLiveEvent<NoticeData>()

    val title = ObservableField<String>()
    val message = ObservableField<String>()
    val mdFileName = ObservableField<String>()

    val addCompleteEvent = SingleLiveEvent<NoticeData>()
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

    fun getLastDate() =
        _noticeList.value?.lastOrNull()?.createDate == lastDate

    fun initNoticeData(noticeData: NoticeData?) {
        this.noticeData = noticeData
    }

    fun uploadNotice(title: String, message: String, mdFile: String) {

        val noticeData = NoticeData().addNoticeData(title, message, mdFile)

        addNotice(noticeData)
    }

    private fun addNotice(noticeData: NoticeData) {

        loadingEvent.value = true

        viewModelScope.launch {
            noticeRepository.setNotice(noticeData,
                success = {
                    addCompleteEvent.value = noticeData
                    loadingEvent.value = false
                },
                fail = {

                    //TODO 에러메세지 필요
                    errorEvent.value = 0
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