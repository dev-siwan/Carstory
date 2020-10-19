package com.like.drive.carstory.repository.notification

import com.like.drive.carstory.cache.dao.notification.NotificationDao
import com.like.drive.carstory.cache.entity.NotificationEntity
import com.like.drive.carstory.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImpl(private val dao: NotificationDao) : NotificationRepository {

    override suspend fun insert(notificationSendData: NotificationSendData): Long {
        return dao.insert(NotificationEntity().dataToEntity(notificationSendData))
    }

    override fun getList(): Flow<List<NotificationEntity>> {
        return dao.getList()
    }

    override suspend fun allDelete() {
        dao.deleteNotification()
    }

}