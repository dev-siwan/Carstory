package com.like.drive.motorfeed.remote.api.motor

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow

class MotorTypeApiImpl(private val fireBaseTask: FireBaseTask,
                       private val fireStore: FirebaseFirestore) :MotorTypeApi{

    override suspend fun getMotorTypeList(): Flow<List<MotorTypeData>> {
        return fireBaseTask.getData(fireStore.collection(CollectionName.MOTOR_TYPE_LIST),
            MotorTypeData::class.java)
    }

}