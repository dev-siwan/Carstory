package com.like.drive.motorfeed.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.remote.api.user.UserApi

class UserRepositoryImpl(private val userApi: UserApi) :UserRepository{

    override suspend fun getUser(): ResultState<UserData> {
        return userApi.getUser()
    }

    override suspend fun checkUser()= userApi.checkUser()
    override suspend fun signFaceBook(
        authCredential: AuthCredential,
        success: (FirebaseUser) -> Unit,
        fail: () -> Unit
    ) {
        userApi.facebookLogin(authCredential).let {
            when(it){
                is ResultState.Success ->{
                    it.data.user?.let {user->
                        success(user)
                    }?:fail()
                }
                is ResultState.Error->{
                    fail.invoke()
                }
            }
        }
    }

}