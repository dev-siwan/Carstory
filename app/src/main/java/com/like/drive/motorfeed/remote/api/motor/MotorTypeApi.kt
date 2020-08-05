package com.like.drive.motorfeed.remote.api.motor

import com.like.drive.motorfeed.data.motor.MotorTypeData
import kotlinx.coroutines.flow.Flow

interface MotorTypeApi{
    suspend fun getMotorTypeList(): Flow<List<MotorTypeData>>
}