package com.like.drive.motorfeed.ui.report.reg.data

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.like.drive.motorfeed.R

data class ReportType(
    val title: String? = null,
    val checked: ObservableBoolean = ObservableBoolean(false)
) {
    fun getList(context: Context) = context.resources.getStringArray(R.array.pick_report_type)
        .map { ReportType(it) }
}

