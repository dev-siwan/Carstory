package com.like.drive.carstory.remote.api.notification

import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.remote.common.FireBaseTask
import kotlinx.coroutines.flow.Flow

class NotificationApiImpl (
    private val fireBaseTask: FireBaseTask
):NotificationApi{
    override suspend fun sendNotification(notificationSendData: NotificationSendData): Flow<String> {
        return fireBaseTask.setFunction(notificationSendData.getHashMap(),"notification")
    }

}