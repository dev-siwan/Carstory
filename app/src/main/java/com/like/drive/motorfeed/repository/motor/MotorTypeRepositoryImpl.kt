package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.cache.dao.motor.MotorTypeDao
import com.like.drive.motorfeed.cache.entity.MotorTypeEntity
import com.like.drive.motorfeed.common.ResultState
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.motor.MotorTypeList
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MotorTypeRepositoryImpl(
    private val motorTypeApi: MotorTypeApi,
    private val motorTypeDao: MotorTypeDao
) : MotorTypeRepository {

    /*
    * 자동차 리스트 들고 오는 api
    */
    override suspend fun setMotorTypeList() = withContext(Dispatchers.IO) {

        motorTypeApi.getMotorTypeList().let {
            if (it is ResultState.Success) {
                val motorTypeList = if (it.data.isNotEmpty()) {
                    MotorTypeList().motorTypeList.apply {
                        addAll(it.data)
                    }
                } else {
                    MotorTypeList().motorTypeList
                }
                insertMotorType(motorTypeList)
            } else {
                insertMotorType(MotorTypeList().motorTypeList)
            }
        }
    }

    private suspend fun insertMotorType(list:List<MotorTypeData>){
        motorTypeDao.replaceList(
            list.map { MotorTypeEntity().dataToEntity(it)})
    }


}

