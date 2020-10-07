package com.like.drive.carstory.repository.motor

import com.like.drive.carstory.data.motor.MotorTypeData


interface MotorTypeRepository{
    suspend fun setMotorTypeList(success:()->Unit,error:(String)->Unit)
    suspend fun isNotEmptyMotorTypeList():Boolean
    suspend fun getMotorTypeList():List<MotorTypeData>
    suspend fun searchMotorTypeList(q:String):List<MotorTypeData>
    suspend fun brandCodeByList(code:Int):List<MotorTypeData>
}