package com.like.drive.motorfeed.remote.api.user

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.like.drive.motorfeed.data.user.UserData
import kotlinx.coroutines.flow.Flow

interface UserApi {
    suspend fun getUser(): Flow<UserData?>
    suspend fun checkUser():Boolean
    suspend fun loginFacebook(authCredential: AuthCredential):Flow<AuthResult>
    suspend fun setUser(userData: UserData):Flow<Boolean>
    suspend fun signEmail(email:String,password:String):Flow<AuthResult>
    suspend fun loginEmail(email:String,password:String):Flow<AuthResult>
    suspend fun signOut()
    suspend fun setUserProfile(uid:String,nickName:String,imgPath: String?=null,intro:String?=null):Flow<Boolean>
    suspend fun checkNickName(nickName: String):Flow<List<UserData>>
    suspend fun updateFcmToken(token:String):Flow<Boolean>
}