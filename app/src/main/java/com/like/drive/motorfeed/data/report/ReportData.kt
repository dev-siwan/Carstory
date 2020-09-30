package com.like.drive.motorfeed.data.report

import com.like.drive.motorfeed.data.user.UserData
import java.util.*

data class ReportData(
    var rid: String? = null,
    val bid: String? = null,
    val userInfo: UserData? = null,
    val type: String? = null,
    val title: String? = null,
    val createDate: Date? = Date()
)