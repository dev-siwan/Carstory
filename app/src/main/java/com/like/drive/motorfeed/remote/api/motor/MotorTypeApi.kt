package com.like.drive.motorfeed.remote.api.motor

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.motor.MotorTypeData

interface MotorTypeApi{
    suspend fun getMotorTypeList(): ResultState<List<MotorTypeData>>
}