package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.repository.base.ResultRepository
import java.lang.Exception

interface MotorTypeRepository{
    suspend fun setMotorTypeList(success:()->Unit,error:(e:Exception)->Unit)
}