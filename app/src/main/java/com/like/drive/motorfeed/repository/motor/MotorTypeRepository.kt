package com.like.drive.motorfeed.repository.motor

interface MotorTypeRepository{
    suspend fun setMotorTypeList(success:()->Unit,error:(e:Exception)->Unit)
    suspend fun isNotEmptyMotorTypeList():Boolean
}