package com.like.drive.carstory.data.notification

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.like.drive.carstory.CarStoryApplication
import com.like.drive.carstory.R
import com.like.drive.carstory.cache.entity.NotificationEntity
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class NotificationSendData(
    @SerializedName("mId")
    val mid: Long? = null,
    @SerializedName("notificationType")
    val notificationType: String? = null,
    @SerializedName("nid")
    val nid: String? = null,
    @SerializedName("bid")
    val bid: String? = null,
    @SerializedName("uid")
    val uid: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("createDate")
    val createDate: Date? = null
) : Parcelable {
    fun getHashMap() =
        mapOf(
            "notificationType" to notificationType,
            "nid" to nid,
            "bid" to bid,
            "uid" to uid,
            "title" to title,
            "message" to message
        )

    fun entityToData(entity: NotificationEntity) = NotificationSendData(
        mid = entity.mID,
        notificationType = entity.notificationType,
        nid = entity.nid,
        bid = entity.bid,
        uid = entity.uid,
        title = entity.title,
        message = entity.message,
        createDate = entity.createData
    )
}

enum class NotificationType(val value: String, val title: String) {
    NOTICE("0", CarStoryApplication.getContext().getString(R.string.fcm_notice_title)),
    COMMENT("1", CarStoryApplication.getContext().getString(R.string.fcm_comment_title)),
    RE_COMMENT("2", CarStoryApplication.getContext().getString(R.string.fcm_re_comment_title));

}