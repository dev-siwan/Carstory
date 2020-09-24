package com.like.drive.motorfeed.repository.notification

import com.like.drive.motorfeed.cache.entity.NotificationEntity
import com.like.drive.motorfeed.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun insert(notificationSendData: NotificationSendData): Long
    fun getList(): Flow<List<NotificationEntity>>
    suspend fun allDelete()
}