package com.like.drive.motorfeed.repository.motor

import com.like.drive.motorfeed.cache.dao.motor.MotorTypeDao
import com.like.drive.motorfeed.cache.entity.MotorTypeEntity
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.motor.MotorTypeList
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class MotorTypeRepositoryImpl(
    private val motorTypeApi: MotorTypeApi,
    private val motorTypeDao: MotorTypeDao
) : MotorTypeRepository {

    /*
    * 자동차 리스트 들고 오는 api
    */
    override suspend fun setMotorTypeList(success: () -> Unit, error: (String) -> Unit) =
        withContext(Dispatchers.IO) {
            motorTypeApi.getMotorTypeList()
                .catch { it.message?.let {message-> error.invoke(message) } }
                .collect {
                val motorTypeList = makeList(it)
                insertMotorType(motorTypeList).catch { e ->
                    e.message?.let {message-> error.invoke(message)}
                }.collect()
                success.invoke()
            }
        }


    override suspend fun isNotEmptyMotorTypeList(): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            motorTypeDao.getMotorType().isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getMotorTypeList(): List<MotorTypeData> {
        return motorTypeDao.getMotorType().map { MotorTypeData().entityToData(it) }
    }

    override suspend fun searchMotorTypeList(q: String): List<MotorTypeData> {
        return motorTypeDao.searchMotorType(q).map { MotorTypeData().entityToData(it) }
    }

    override suspend fun brandCodeByList(code: Int): List<MotorTypeData> {
        return motorTypeDao.selectType(code).map { MotorTypeData().entityToData(it) }
    }

    private fun makeList(list: List<MotorTypeData>): MutableList<MotorTypeData> {
        return if (list.isNotEmpty()) {
            MotorTypeList().motorTypeList.apply {
                addAll(list)
            }
        } else {
            MotorTypeList().motorTypeList
        }
    }

    private suspend fun insertMotorType(list: List<MotorTypeData>): Flow<Unit> =
        flow {
            motorTypeDao.replaceList(list.map { MotorTypeEntity().dataToEntity(it) })
            emit(Unit)
        }


}


