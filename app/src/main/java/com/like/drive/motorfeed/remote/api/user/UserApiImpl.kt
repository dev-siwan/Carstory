package com.like.drive.motorfeed.remote.api.user

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.common.ResultState
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName.USER

class UserApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth
) : UserApi {
    override suspend fun getUser(): ResultState<UserData> {
        return fireBaseTask.getData(
            fireStore.collection(USER).document(fireAuth.uid!!),
            UserData::class.java
        )
    }

}