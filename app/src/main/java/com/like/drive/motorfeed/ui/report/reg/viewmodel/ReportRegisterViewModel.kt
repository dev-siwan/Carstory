package com.like.drive.motorfeed.ui.report.reg.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.report.reg.data.ReportType

class ReportRegisterViewModel : BaseViewModel() {

    private val _reportTypes =
        MutableLiveData<List<ReportType>>(ReportType().getList(MotorFeedApplication.getContext()))
    val reportTypes: LiveData<List<ReportType>> get() = _reportTypes

    val isCompleteEnable = ObservableBoolean(false)

    val onCompleteEvent = SingleLiveEvent<ReportType>()

    fun onClickItemListener(reportType: ReportType) {
        _reportTypes.value?.forEach {
            if (it == reportType) {
                it.checked.set(true)
            } else {
                it.checked.set(false)
            }
        }

        isCompleteEnable.set(_reportTypes.value?.any { it.checked.get() } == true)
    }

    fun onCompleteClickListener() {
        onCompleteEvent.value = _reportTypes.value?.find { it.checked.get() }
    }

}