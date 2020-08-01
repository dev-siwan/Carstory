package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.data.motor.MotorTypeData


interface MotorTypeRepository{
    suspend fun setMotorTypeList(success:()->Unit,error:(String)->Unit)
    suspend fun isNotEmptyMotorTypeList():Boolean
    suspend fun getMotorTypeList():List<MotorTypeData>
    suspend fun searchMotorTypeList(q:String):List<MotorTypeData>
    suspend fun brandCodeByList(code:Int):List<MotorTypeData>
}