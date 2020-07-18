package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.cache.dao.motor.MotorTypeDao
import com.like.drive.motorfeed.cache.entity.MotorTypeEntity
import com.like.drive.motorfeed.common.async.ResultState
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
    override suspend fun setMotorTypeList(success:()->Unit,error:(e: Exception)->Unit)= withContext(Dispatchers.IO) {

        motorTypeApi.getMotorTypeList().let { result->
            when (result) {
                is ResultState.Success -> {
                    val motorTypeList =makeList(result.data)
                    insertMotorType(motorTypeList).let {insertCacheResult->
                        if(insertCacheResult is ResultState.Error){
                            error(insertCacheResult.exception!!)
                            return@withContext
                        }
                        success.invoke()
                    }

                }
                is ResultState.Error -> {
                    error(result.exception!!)
                }
            }
        }
    }

    override suspend fun isNotEmptyMotorTypeList(): Boolean = withContext(Dispatchers.IO) {
        return@withContext  try {
            motorTypeDao.getMotorType().isNotEmpty()
        } catch (e: Exception) {
            false
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
    private suspend fun insertMotorType(list:List<MotorTypeData>):ResultState<Unit> = withContext(Dispatchers.IO) {
        try {
            motorTypeDao.replaceList(
                list.map { MotorTypeEntity().dataToEntity(it) })
            return@withContext ResultState.Success(Unit)
        }catch (e:Exception){
           return@withContext ResultState.Error(e)
        }
    }


}


