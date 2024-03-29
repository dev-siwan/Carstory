package com.like.drive.carstory.cache.dao.like

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.like.drive.carstory.cache.entity.LikeEntity

@Dao
interface LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBid(bid: LikeEntity)

    @Query("DELETE FROM LikeEntity WHERE bid =:bid")
    suspend fun deleteBid(bid: String)

    @Query("DELETE FROM LikeEntity")
    suspend fun deleteLike()

    @Query("SELECT * FROM LikeEntity WHERE bid =:bid")
    suspend fun isBoardLike(bid: String): List<LikeEntity>
}