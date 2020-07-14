package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.cache.dao.motor.MotorTypeDao
import com.like.drive.motorfeed.cache.entity.MotorTypeEntity
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.motor.MotorTypeList
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApi
import com.like.drive.motorfeed.repository.base.ResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MotorTypeRepositoryImpl(
    private val motorTypeApi: MotorTypeApi,
    private val motorTypeDao: MotorTypeDao
) : MotorTypeRepository {

    /*
    * 자동차 리스트 들고 오는 api
    */
    override suspend fun setMotorTypeList(success:()->Unit,error:(e: Exception)->Unit)= withContext(Dispatchers.IO) {

        motorTypeApi.getMotorTypeList().let { result->
            when (result) {
                is ResultState.Success -> {
                    val motorTypeList =makeList(result.data)
                    insertMotorType(motorTypeList)
                    success.invoke()
                }
                is ResultState.Error -> {
                    error(result.exception!!)
                }
            }
        }
    }

    private fun makeList(list:List<MotorTypeData>):MutableList<MotorTypeData>{
      return if (list.isNotEmpty()) {
            MotorTypeList().motorTypeList.apply {
                addAll(list)
            }
        } else {
            MotorTypeList().motorTypeList
        }
    }
    private suspend fun insertMotorType(list:List<MotorTypeData>){
        motorTypeDao.replaceList(
            list.map { MotorTypeEntity().dataToEntity(it) })
    }


}


