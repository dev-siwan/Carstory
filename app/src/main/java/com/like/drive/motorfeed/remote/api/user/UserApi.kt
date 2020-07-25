package com.like.drive.motorfeed.remote.api.user

import com.facebook.AccessToken
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.user.UserData

interface UserApi {
    suspend fun getUser(): ResultState<UserData>
    suspend fun checkUser():Boolean
    suspend fun loginFacebook(authCredential: AuthCredential):ResultState<AuthResult>
    suspend fun setUser(userData: UserData):ResultState<Boolean>
    suspend fun signEmail(email:String,password:String):ResultState<AuthResult>
    suspend fun loginEmail(email:String,password:String):ResultState<AuthResult>
    suspend fun signOut()
}