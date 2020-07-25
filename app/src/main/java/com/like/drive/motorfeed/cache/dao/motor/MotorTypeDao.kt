package com.like.drive.motorfeed.cache.dao.motor

import androidx.room.*
import com.like.drive.motorfeed.cache.entity.MotorTypeEntity

@Dao
interface MotorTypeDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(dataList: List<MotorTypeEntity>)

    @Transaction
    suspend fun replaceList(dataList: List<MotorTypeEntity>) {
        if(dataList.isNotEmpty()) {
            deleteAll()
            insertList(dataList = dataList)
        }
    }

    @Query("SELECT * FROM MotorTypeEntity")
    suspend fun getMotorType(): List<MotorTypeEntity>

    @Query("SELECT * FROM MotorTypeEntity group by brandCode")
    suspend fun selectMotorType(): List<MotorTypeEntity>

    @Query("SELECT * FROM MotorTypeEntity WHERE brandCode =:code")
    suspend fun selectType(code:Int):List<MotorTypeEntity>

    @Query("SELECT * FROM MotorTypeEntity WHERE brandName LIKE '%' || :query || '%' OR modelName LIKE'%' || :query || '%'")
    suspend fun searchMotorType(query:String): List<MotorTypeEntity>

    @Query("DELETE FROM MotorTypeEntity")
    suspend fun deleteAll()
}