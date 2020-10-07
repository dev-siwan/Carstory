package com.like.drive.carstory.remote.api.motor

import com.like.drive.carstory.data.motor.MotorTypeData
import kotlinx.coroutines.flow.Flow

interface MotorTypeApi{
    suspend fun getMotorTypeList(): Flow<List<MotorTypeData>>
}