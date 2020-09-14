package com.like.drive.motorfeed.cache.dao.notification

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.drive.motorfeed.cache.entity.NotificationEntity

@Dao
interface NotificationDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(data:NotificationEntity):Long

    @Query("SELECT * FROM NotificationEntity ORDER BY createData DESC")
    suspend fun getList():List<NotificationEntity>
}