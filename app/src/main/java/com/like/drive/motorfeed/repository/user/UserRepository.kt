package com.like.drive.motorfeed.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.user.UserData

interface UserRepository{
    suspend fun getUser(): ResultState<UserData>
    suspend fun checkUser():Boolean
    suspend fun signFaceBook(authCredential: AuthCredential,success:(FirebaseUser)->Unit,error:()->Unit)
}