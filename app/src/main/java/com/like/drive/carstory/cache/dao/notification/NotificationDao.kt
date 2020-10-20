package com.like.drive.carstory.cache.dao.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.drive.carstory.cache.entity.NotificationEntity
import com.like.drive.carstory.ui.base.ext.getDaysAgo
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: NotificationEntity): Long

    @Query("SELECT * FROM NotificationEntity WHERE createData BETWEEN :agoDate AND :nowDate ORDER BY createData DESC")
    fun getList(
        nowDate: Date = Date(),
        agoDate: Date = getDaysAgo(15)
    ): Flow<List<NotificationEntity>>

    @Query("DELETE FROM NotificationEntity")
    suspend fun deleteNotification()

    @Query("DELETE FROM NotificationEntity WHERE mID =:id")
    suspend fun deleteNotificationItem(id: Int)

}