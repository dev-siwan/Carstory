package com.like.drive.motorfeed.cache.dao.notification

import androidx.room.*
import com.like.drive.motorfeed.cache.entity.NotificationEntity

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: NotificationEntity): Long

    @Query("SELECT * FROM NotificationEntity ORDER BY createData DESC")
    suspend fun getList(): List<NotificationEntity>

    @Query("DELETE FROM NotificationEntity")
    suspend fun deleteList()
}