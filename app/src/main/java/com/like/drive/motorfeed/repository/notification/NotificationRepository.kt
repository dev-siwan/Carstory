package com.like.drive.motorfeed.repository.notification

import com.like.drive.motorfeed.data.notification.NotificationSendData

interface NotificationRepository{

    suspend fun insert(notificationSendData: NotificationSendData):Long
    suspend fun getList():List<NotificationSendData>
}