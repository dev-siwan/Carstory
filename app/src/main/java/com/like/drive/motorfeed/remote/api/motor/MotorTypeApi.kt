package com.like.drive.motorfeed.remote.api.motor

import com.like.drive.motorfeed.common.ResultState
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.common.Version

interface MotorTypeApi{
    suspend fun getMotorTypeList():ResultState<List<MotorTypeData>>
}