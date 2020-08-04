package com.like.drive.motorfeed.repository.user

import android.net.Uri
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.api.user.UserApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import java.io.File

class UserRepositoryImpl(private val userApi: UserApi, private val imageApi: ImageApi) :UserRepository{

    override suspend fun getUser(
        success: () -> Unit,
        fail: () -> Unit,
        userBan: () -> Unit,
        emptyUser: () -> Unit
    ) {
        userApi.getUser()
            .catch { fail.invoke() }
            .collect { userData ->
                userData?.let {
                    when {
                        it.userBan -> userBan.invoke()
                        else -> {
                            UserInfo.userInfo = it
                            success.invoke()
                        }
                    }
                } ?: emptyUser.invoke()
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

    override suspend fun updateProfile(
        nickName: String,
        imgFile: File?,
        intro: String?,
        success: (Uri?) -> Unit,
        fail: () -> Unit,
        notUser: () -> Unit
    ) {
        UserInfo.userInfo?.uid?.let { uid ->
            var imgUri: Uri? = null

            imgFile?.let {
                imgUri = imageApi.profileImage(uid, it).catch { fail.invoke() }.single()
            }

            userApi.setUserProfile(uid, nickName, imgUri?.toString(), intro)
                .catch { fail.invoke() }.collect { success(imgUri) }

        } ?: notUser.invoke()

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