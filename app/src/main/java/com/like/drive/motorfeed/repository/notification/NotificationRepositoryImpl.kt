package com.like.drive.motorfeed.repository.notification

import com.like.drive.motorfeed.cache.dao.notification.NotificationDao
import com.like.drive.motorfeed.cache.entity.NotificationEntity
import com.like.drive.motorfeed.data.notification.NotificationSendData

class NotificationRepositoryImpl(private val dao: NotificationDao) : NotificationRepository {

    override suspend fun insert(notificationSendData: NotificationSendData): Long {
        return dao.insert(NotificationEntity().dataToEntity(notificationSendData))
    }

    override suspend fun getList(): List<NotificationSendData> {
        return dao.getList().map { NotificationSendData().entityToData(it) }
    }

    override suspend fun allDelete() {
        dao.deleteList()
    }

}