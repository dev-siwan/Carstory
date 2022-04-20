package com.like.drive.carstory.remote.api.motor

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.remote.task.FireBaseTask
import com.like.drive.carstory.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MotorTypeApiImpl @Inject constructor(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : MotorTypeApi {

    override suspend fun getMotorTypeList(): Flow<List<MotorTypeData>> {
        return fireBaseTask.getData(
            fireStore.collection(CollectionName.MOTOR_TYPE_LIST),
            MotorTypeData::class.java
        )
    }

}