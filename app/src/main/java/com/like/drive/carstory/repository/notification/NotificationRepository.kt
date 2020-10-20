package com.like.drive.carstory.repository.notification

import com.like.drive.carstory.cache.entity.NotificationEntity
import com.like.drive.carstory.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    suspend fun insert(notificationSendData: NotificationSendData): Long
    fun getList(): Flow<List<NotificationEntity>>
    suspend fun deleteItem(id:Int)
    suspend fun deleteAll()
}