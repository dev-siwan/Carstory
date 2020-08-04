package com.like.drive.motorfeed.remote.api.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName.USER
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class UserApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore,
    private val fireAuth: FirebaseAuth
) : UserApi {
    override suspend fun getUser(): Flow<UserData?> {
        return fireBaseTask.getData(
            fireStore.collection(USER).document(fireAuth.uid!!),
            UserData::class.java
        )
    }

    override suspend fun checkUser(): Boolean {
        return fireAuth.currentUser != null
    }

    override suspend fun loginFacebook(authCredential: AuthCredential): Flow<AuthResult> =
        flow { emit(fireAuth.signInWithCredential(authCredential).await()) }

    override suspend fun setUser(userData: UserData): Flow<Boolean> {
        return fireBaseTask.setData(fireStore.collection(USER).document(fireAuth.uid!!), userData)
    }

    override suspend fun signEmail(email: String, password: String): Flow<AuthResult> =
        flow { emit(fireAuth.createUserWithEmailAndPassword(email, password).await()) }


    override suspend fun loginEmail(email: String, password: String): Flow<AuthResult> =
        flow { emit(fireAuth.signInWithEmailAndPassword(email, password).await()) }


    override suspend fun signOut() {
        fireAuth.currentUser?.let {
            fireAuth.signOut()
        }
    }


    override suspend fun setUserProfile(
        uid: String,
        nickName: String,
        imgPath: String?,
        intro: String?
    ): Flow<Boolean> {
        val document = fireStore.collection(USER).document(uid)

        val map = mutableMapOf("nickName" to nickName)
        imgPath?.let {
            map.put("profileImgUrl", it)
        }
        intro?.let {
            map.put("intro", it)
        }

        return fireBaseTask.updateData(document, map)
    }
}