package com.like.drive.motorfeed.remote.api.version

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.common.Version
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.remote.reference.DocumentName

class VersionApiImpl (private val fireBaseTask: FireBaseTask, private val firestore: FirebaseFirestore):VersionApi{
    override suspend fun getMotorTypeVersion() =
        fireBaseTask.getData(firestore.collection(CollectionName.VERSION_UTIL).document(DocumentName.MOTOR_TYPE_VERSION),Version::class.java)

}