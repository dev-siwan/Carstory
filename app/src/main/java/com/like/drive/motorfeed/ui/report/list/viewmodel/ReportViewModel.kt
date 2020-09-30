package com.like.drive.motorfeed.ui.report.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.report.ReportData
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.repository.report.ReportRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class ReportViewModel(private val reportRepository: ReportRepository) : BaseViewModel() {

    private val _reportList = MutableLiveData<List<ReportData>>()
    val reportList: LiveData<List<ReportData>> get() = _reportList

    var loadingStatus: LoadingStatus? = null

    val isRefresh = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isMore = ObservableBoolean(false)

    var isFirst = true
    private var lastDate: Date? = null

    val removeListenerEvent = SingleLiveEvent<ReportData>()
    val removeCompleteEvent = SingleLiveEvent<ReportData>()
    val detailBoardEvent = SingleLiveEvent<String>()
    val userLookUpEvent = SingleLiveEvent<UserData>()
    val errorEvent = SingleLiveEvent<@StringRes Int>()

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
            reportRepository.getReportList(date ?: Date())
                .catch {
                    loadingStatus()
                    errorEvent.value = R.string.report_get_list_error_message
                }
                .collect {
                    _reportList.value = it
                    loadingStatus()
                }
        }
    }

    fun getLastDate() = _reportList.value?.lastOrNull()?.createDate == lastDate

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

    fun removeListener(reportData: ReportData) {
        removeListenerEvent.value = reportData
    }

    fun boardDetailListener(bid: String) {
        detailBoardEvent.value = bid
    }

    fun userLookUpListener(userData: UserData) {
        userLookUpEvent.value = userData
    }

    fun removeReport(reportData: ReportData) {
        isLoading.set(true)
        viewModelScope.launch {
            reportRepository.removeReport(reportData,
                success = {
                    removeCompleteEvent.value = reportData
                },
                fail = {
                    errorEvent.value = R.string.report_remove_error_message
                })
        }
    }

}