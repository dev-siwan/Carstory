package com.like.drive.carstory.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.like.drive.carstory.data.motor.MotorTypeData

@Entity
data class MotorTypeEntity(
    @PrimaryKey(autoGenerate = true)
    val mID: Int?=null,
    val brandName: String = "",
    val brandCode: Int = 0,
    val modelName: String = "",
    val modelCode: Int = 0
) {
    fun dataToEntity(data: MotorTypeData) =
        MotorTypeEntity(
            brandCode = data.brandCode,
            brandName = data.brandName,
            modelName = data.modelName,
            modelCode = data.modelCode
        )
}
