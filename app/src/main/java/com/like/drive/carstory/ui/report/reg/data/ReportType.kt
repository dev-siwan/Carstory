package com.like.drive.carstory.ui.report.reg.data

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.like.drive.carstory.R

data class ReportType(
    val title: String? = null,
    val checked: ObservableBoolean = ObservableBoolean(false)
) {
    fun getList(context: Context) = context.resources.getStringArray(R.array.pick_report_type)
        .map { ReportType(it) }
}

