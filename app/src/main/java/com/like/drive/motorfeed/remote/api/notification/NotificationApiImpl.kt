package com.like.drive.motorfeed.remote.api.notification

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.notification.NotificationSendData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import kotlinx.coroutines.flow.Flow

class NotificationApiImpl (
    private val fireBaseTask: FireBaseTask
):NotificationApi{
    override suspend fun sendNotification(notificationSendData: NotificationSendData): Flow<String> {
        return fireBaseTask.setFunction(notificationSendData.getHashMap(),"notification")
    }

}