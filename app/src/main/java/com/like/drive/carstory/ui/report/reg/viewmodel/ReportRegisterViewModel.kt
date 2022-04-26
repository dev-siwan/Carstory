package com.like.drive.carstory.ui.report.reg.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.carstory.CarStoryApplication
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.report.reg.data.ReportType
import javax.inject.Inject

class ReportRegisterViewModel @Inject constructor() : BaseViewModel() {

    private val _reportTypes =
        MutableLiveData<List<ReportType>>(ReportType().getList(CarStoryApplication.getContext()))
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