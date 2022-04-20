package com.like.drive.carstory.remote.api.notification

import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.remote.task.FireBaseTask
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NotificationApiImpl @Inject constructor(
    private val fireBaseTask: FireBaseTask
):NotificationApi{
    override suspend fun sendNotification(notificationSendData: NotificationSendData): Flow<Any> {
        return fireBaseTask.setFunction(notificationSendData.getHashMap(),"notification")
    }

}