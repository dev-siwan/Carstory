package com.like.drive.carstory.repository.notification

import com.like.drive.carstory.cache.entity.NotificationEntity
import com.like.drive.carstory.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun insert(notificationSendData: NotificationSendData): Long
    suspend fun getList(): List<NotificationEntity>
    suspend fun deleteItem(id:Long)
    suspend fun deleteAll()
}