package com.like.drive.motorfeed.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.remote.api.user.UserApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class UserRepositoryImpl(private val userApi: UserApi) :UserRepository{

    override suspend fun getUser(
        success: () -> Unit,
        fail: () -> Unit,
        userBan: () -> Unit,
        empty: () -> Unit
    ) {
        userApi.getUser()
            .catch { fail.invoke() }
            .collect { userData ->
                userData?.let {
                    if (it.userBan) {
                        userBan.invoke()
                    } else {
                        UserInfo.userInfo = it
                        success.invoke()
                    }
                } ?: empty.invoke()
            }
    }

    override suspend fun checkUser()= userApi.checkUser()

    override suspend fun loginFaceBook(
        authCredential: AuthCredential,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.loginFacebook(authCredential).
            catch {error.invoke()}
            .collect {
                it.user?.let {user->
                    success(user)
                }?:error()
            }
    }

    override suspend fun setUser(userData: UserData, success: () -> Unit, fail: () -> Unit) {
        userApi.setUser(userData).catch {
            it.printStackTrace()
            fail.invoke()
        }.collect { isComplete ->
            if (isComplete) {
                UserInfo.userInfo = userData
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

    override suspend fun signEmail(
        email: String, password: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.signEmail(email, password).catch {
            it.printStackTrace()
            error.invoke()
        }.collect {
            it.user?.let { user ->
                success(user)
            } ?: error()
        }

    }

    override suspend fun loginEmail(
        email: String,
        password: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.loginEmail(email, password).catch {
            it.printStackTrace()
            error.invoke()
        }.collect {
            it.user?.let { user ->
                success(user)
            } ?: error()
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