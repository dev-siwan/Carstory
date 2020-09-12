package com.like.drive.motorfeed.remote.api.notification

import com.like.drive.motorfeed.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow

interface NotificationApi {
    suspend fun sendNotification(notificationSendData: NotificationSendData):Flow<String>
}