package com.like.drive.motorfeed.repository.notification

import com.like.drive.motorfeed.cache.dao.notification.NotificationDao
import com.like.drive.motorfeed.cache.entity.NotificationEntity
import com.like.drive.motorfeed.data.notification.NotificationSendData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(private val dao: NotificationDao) : NotificationRepository {

    override suspend fun insert(notificationSendData: NotificationSendData): Long {
        return dao.insert(NotificationEntity().dataToEntity(notificationSendData))
    }

    override fun getList(): Flow<List<NotificationEntity>> {
        return dao.getList()
    }

    override suspend fun allDelete() {
        dao.deleteList()
    }

}