package com.like.drive.carstory.repository.notification

import com.like.drive.carstory.cache.dao.notification.NotificationDao
import com.like.drive.carstory.cache.entity.NotificationEntity
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.ui.base.ext.getDaysAgo
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(private val dao: NotificationDao) :
    NotificationRepository {

    override suspend fun insert(notificationSendData: NotificationSendData): Long {
        return dao.insert(NotificationEntity().dataToEntity(notificationSendData))
    }

    override suspend fun getList(): List<NotificationEntity> {
        return dao.getList()
    }

    override suspend fun deleteItem(id: Long) {
        dao.deleteNotificationItem(id)
    }

    override suspend fun deleteAll() {
        dao.deleteNotification()
    }

}