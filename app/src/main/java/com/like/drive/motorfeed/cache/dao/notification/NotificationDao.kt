package com.like.drive.motorfeed.cache.dao.notification

import androidx.room.*
import com.like.drive.motorfeed.cache.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data: NotificationEntity): Long

    @Query("SELECT * FROM NotificationEntity ORDER BY createData DESC")
    fun getList(): Flow<List<NotificationEntity>>

    @Query("DELETE FROM NotificationEntity")
    suspend fun deleteList()
}