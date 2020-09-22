package com.like.drive.motorfeed.cache.dao.like

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.drive.motorfeed.cache.entity.LikeEntity

@Dao
interface LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFid(fid: LikeEntity)

    @Query("DELETE FROM LikeEntity WHERE fid =:fid")
    suspend fun deleteFid(fid: String)

    @Query("DELETE FROM LikeEntity")
    suspend fun deleteLike()

    @Query("SELECT * FROM LikeEntity WHERE fid =:fid")
    suspend fun isFeedEmpty(fid: String): List<LikeEntity>
}