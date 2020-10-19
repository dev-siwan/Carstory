package com.like.drive.carstory.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.remote.api.img.ImageApi
import com.like.drive.carstory.remote.api.user.UserApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import java.io.File

class UserRepositoryImpl(private val userApi: UserApi, private val imageApi: ImageApi) :
    UserRepository {

    override suspend fun getUser(
        success: (UserData) -> Unit,
        fail: () -> Unit,
        emptyUser: () -> Unit
    ) {
        userApi.getUser()
            .catch { e ->
                e.printStackTrace()
                fail.invoke()
            }
            .collect { userData ->
                userData?.let {
                    UserInfo.run {
                        userInfo = it
                        updateFcm(it.fcmToken)
                    }
                    success.invoke(it)
                } ?: emptyUser.invoke()
            }
    }

    override suspend fun getUserProfile(
        uid: String,
        success: (UserData?) -> Unit,
        fail: () -> Unit
    ) {
        userApi.getUserProfile(uid).catch { e ->
            e.printStackTrace()
            fail.invoke()
        }.collect { success(it) }
    }

    override suspend fun checkUser() = userApi.checkUser()

    override suspend fun loginCredential(
        authCredential: AuthCredential,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.loginCredential(authCredential).catch { error.invoke() }
            .collect {
                it.user?.let { user ->
                    success(user)
                } ?: error()
            }
    }

    override suspend fun createKaKaoCustomToken(
        uid: String,
        success: (String) -> Unit,
        error: () -> Unit
    ) {
        userApi.createToken(uid).catch {
            it.message
            error.invoke()
        }.collect {
            (it as HashMap<String?, String?>?)?.let { map ->
                map["token"]?.let { token ->
                    success(token)
                }
            }
        }
    }

    override suspend fun loginCustomToken(
        token: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    ) {
        userApi.loginCustomToken(token).catch { error.invoke() }
            .collect {
                it.user?.let { user ->
                    success(user)
                } ?: error()
            }
    }

    override suspend fun setUser(userData: UserData, success: () -> Unit, fail: () -> Unit) {
        userApi.setUser(userData).catch {
            it.printStackTrace()
            fail.invoke()
        }.collect { isComplete ->
            if (isComplete) {
                UserInfo.run {
                    setUserData(userData)
                    updateFcm(userData.fcmToken)
                }
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
        existNickName: () -> Unit,
        success: (String?) -> Unit,
        fail: () -> Unit,
        notUser: () -> Unit
    ) {
        UserInfo.userInfo?.let { info ->

            if (nickName != info.nickName) {
                userApi.checkNickName(nickName).catch { fail.invoke() }.single().let {
                    if (it.isNotEmpty()) {
                        existNickName.invoke()
                        return
                    }
                }
            }

            imgFile?.let {

                imageApi.profileImage(info.uid ?: "", it).catch { fail.invoke() }.single()
            }

            val profilePath = info.profileImgPath ?: "/user/${info.uid}/profileImg"

            userApi.setUserProfile(info.uid ?: "", nickName, profilePath, intro)
                .catch { fail.invoke() }.collect { success(profilePath) }

        } ?: notUser.invoke()

    }

    override suspend fun signOut(success: () -> Unit, fail: () -> Unit) {
        try {
            userApi.signOut()
            success.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            fail.invoke()
        }
    }

    override suspend fun resetPassword(email: String, success: () -> Unit, fail: () -> Unit) {
        userApi.resetPassword(email).collect {
            if (it) {
                success.invoke()
            } else {
                fail.invoke()
            }
        }
    }

    override suspend fun updatePassword(
        nowPassword: String,
        rePassword: String, success: () -> Unit, failCredential: () -> Unit, fail: () -> Unit
    ) {

        userApi.checkCredential(nowPassword).catch {

            failCredential.invoke()

        }.collect { credentialResult ->

            if (credentialResult) {

                userApi.updatePassword(rePassword).catch { error ->
                    if (error is FirebaseAuthRecentLoginRequiredException) {
                        failCredential.invoke()
                    }
                }.collect {
                    if (it) {
                        success.invoke()
                    } else {
                        fail.invoke()
                    }
                }
            } else {

                failCredential.invoke()
            }

        }

    }

    override fun isProvider(): Boolean {
        return userApi.checkProvider()
    }

}