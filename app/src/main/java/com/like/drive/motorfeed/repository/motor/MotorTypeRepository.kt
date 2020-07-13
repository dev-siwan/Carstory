package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.repository.base.ResultRepository
import java.lang.Exception

interface MotorTypeRepository{
    suspend fun setMotorTypeList(error:(e:Exception)->Unit)
}