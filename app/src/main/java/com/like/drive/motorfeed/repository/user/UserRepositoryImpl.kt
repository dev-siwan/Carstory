package com.like.drive.motorfeed.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.remote.api.user.UserApi

class UserRepositoryImpl(private val userApi: UserApi) :UserRepository{

    override suspend fun getUser(success: () -> Unit,
                                 fail: () -> Unit,
                                 userBan: () -> Unit,
                                 empty: () -> Unit) {
       userApi.getUser().let {resultState ->
            when(resultState){

                is ResultState.Success -> {
                    if (resultState.data.userBan) {
                        userBan.invoke()
                    } else {
                        UserInfo.userInfo = resultState.data
                        success.invoke()
                    }
                }

                is ResultState.Error -> {
                    resultState.emptyDocument?.let {
                        empty.invoke()
                    }
                    resultState.exception?.let {
                        fail.invoke()
                    }
                }

            }
        }
    }

    override suspend fun checkUser()= userApi.checkUser()

    override suspend fun loginFaceBook(
        authCredential: AuthCredential,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.loginFacebook(authCredential).let {
            when(it){
                is ResultState.Success ->{
                    it.data.user?.let {user->
                        success(user)
                    }?:error()
                }
                is ResultState.Error->{
                    it.exception?.printStackTrace()
                    error.invoke()
                }
            }
        }
    }

    override suspend fun setUser(userData: UserData, success: () -> Unit, fail: () -> Unit) {
        userApi.setUser(userData).let {
            when(it){
                is ResultState.Success ->{
                    if(it.data){
                        UserInfo.userInfo = userData
                        success.invoke()
                    }else{
                        fail.invoke()
                    }
                }
                is ResultState.Error->{
                    it.exception?.printStackTrace()
                    fail.invoke()
                }
            }
        }
    }

    override suspend fun signEmail(
        email: String, password: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.signEmail(email,password).let {
            when(it){
                is ResultState.Success ->{
                    it.data.user?.let {user->
                        success(user)
                    }?:error()
                }
                is ResultState.Error->{
                    it.exception?.printStackTrace()
                    error.invoke()
                }
            }
        }
    }

    override suspend fun loginEmail(
        email: String,
        password: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.loginEmail(email,password).let {
            when(it){
                is ResultState.Success ->{
                    it.data.user?.let {user->
                        success(user)
                    }?:error()
                }
                is ResultState.Error->{
                    it.exception?.printStackTrace()
                    error.invoke()
                }
            }
        }
    }

    override suspend fun signOut(success: () -> Unit, fail: () -> Unit) {
        try{
            userApi.signOut()
            success.invoke()
        }catch (e:Exception){
            e.printStackTrace()
            fail.invoke()
        }
    }


}