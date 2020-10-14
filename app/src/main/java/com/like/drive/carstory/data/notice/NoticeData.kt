package com.like.drive.carstory.data.notice

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
data class NoticeData(
    var nid: String? = null,
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

    fun updateNoticeData(noticeData: NoticeData, title: String, message: String, mdFile: String) =
        NoticeData(
            nid = noticeData.nid,
            title = title,
            message = message,
            mdFile = mdFile,
            createDate = noticeData.createDate,
            uploadDate = Date()
        )
}