package com.like.drive.motorfeed.data.notice

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NoticeData(
    var gid: String? = null,
    val title: String? = null,
    val message: String? = null,
    val mdFile: String? = null,
    val createDate: Date? = null,
    val uploadDate: Date? = null
) : Parcelable {

    fun addNoticeData(title: String, message: String, mdFile: String) = NoticeData(
        title = title,
        message = message,
        mdFile = mdFile,
        createDate = Date(),
        uploadDate = Date()

    )
}