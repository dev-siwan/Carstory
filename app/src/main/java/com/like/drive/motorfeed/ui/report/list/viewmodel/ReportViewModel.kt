package com.like.drive.motorfeed.ui.report.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.data.report.ReportData
import com.like.drive.motorfeed.ui.base.BaseViewModel

class ReportViewModel : BaseViewModel() {

    private val _reportList = MutableLiveData<ReportData>()
    val reportList: LiveData<ReportData> get() = _reportList

}