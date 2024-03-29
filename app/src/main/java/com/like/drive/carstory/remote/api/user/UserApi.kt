package com.like.drive.carstory.remote.api.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.like.drive.carstory.data.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserApi {
    suspend fun getUser(): Flow<UserData?>
    suspend fun getUserProfile(uid: String): Flow<UserData?>
    suspend fun checkUser(): Boolean
    suspend fun loginCredential(authCredential: AuthCredential): Flow<AuthResult>
    suspend fun loginCustomToken(token: String): Flow<AuthResult>
    suspend fun setUser(userData: UserData): Flow<Boolean>
    suspend fun signEmail(email: String, password: String): Flow<AuthResult>
    suspend fun loginEmail(email: String, password: String): Flow<AuthResult>
    suspend fun signOut()
    suspend fun setUserProfile(
        uid: String,
        nickName: String,
        intro: String? = null,
    ): Flow<Boolean>

    suspend fun checkNickName(nickName: String): Flow<List<UserData>>
    suspend fun updateFcmToken(token: String): Flow<Boolean>
    suspend fun updateCommentSubscribe(isSubscribe: Boolean): Flow<Boolean>
    suspend fun checkCredential(password: String): Flow<Boolean>
    suspend fun createToken(uid: String): Flow<Any>
    suspend fun confirmUserMessage(uid: String): Flow<Boolean>
}