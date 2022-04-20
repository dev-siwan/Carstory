package com.like.drive.carstory.remote.api.version

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.carstory.data.common.Version
import com.like.drive.carstory.remote.task.FireBaseTask
import com.like.drive.carstory.remote.reference.CollectionName
import com.like.drive.carstory.remote.reference.DocumentName
import javax.inject.Inject

class VersionApiImpl @Inject constructor(private val fireBaseTask: FireBaseTask, private val firestore: FirebaseFirestore):VersionApi{
    override suspend fun getMotorTypeVersion() =
        fireBaseTask.getData(firestore.collection(CollectionName.VERSION_UTIL).document(DocumentName.MOTOR_TYPE_VERSION),Version::class.java)

}