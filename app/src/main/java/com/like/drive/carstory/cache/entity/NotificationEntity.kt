package com.like.drive.carstory.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.ui.base.ext.getDaysAgo
import java.util.*

@Entity
class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val mID: Long? = null,
    val notificationType: String? = null,
    val nid: String? = null,
    val bid: String? = null,
    val uid: String? = null,
    val title: String? = null,
    val message: String? = null,
    val createData: Date? = null
) {
    fun dataToEntity(data: NotificationSendData) = NotificationEntity(
        notificationType = data.notificationType,
        nid = data.nid,
        bid = data.bid,
        uid = data.uid,
        title = data.title,
        message = data.message,
        createData = Date()
    )

}