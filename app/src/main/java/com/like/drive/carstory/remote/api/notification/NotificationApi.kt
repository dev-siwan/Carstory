package com.like.drive.carstory.remote.api.notification

import com.like.drive.carstory.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow

interface NotificationApi {
    suspend fun sendNotification(notificationSendData: NotificationSendData):Flow<Any>
}