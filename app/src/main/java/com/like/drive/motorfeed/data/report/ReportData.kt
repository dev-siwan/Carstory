package com.like.drive.motorfeed.data.report

import java.util.*

data class ReportData(
    var rid: String? = null,
    val bid: String? = null,
    val uid: String? = null,
    val type: String? = null,
    val title: String? = null,
    val createDate: Date? = Date()
)