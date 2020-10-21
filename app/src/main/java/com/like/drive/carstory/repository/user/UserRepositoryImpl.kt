package com.like.drive.carstory.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.remote.api.img.ImageApi
import com.like.drive.carstory.remote.api.img.ImageApiImpl
import com.like.drive.carstory.remote.api.user.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.withContext
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
                        setUserData(it)
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

            val profileImgPath: String =
                info.profileImgPath ?: "/user/${info.uid}/${ImageApiImpl.USER_IMG_NAME}"

            if (nickName != info.nickName) {
                userApi.checkNickName(nickName).catch { fail.invoke() }.single().let {
                    if (it.isNotEmpty()) {
                        existNickName.invoke()
                        return
                    }
                }
            }

            imgFile?.let {
                withContext(Dispatchers.Default) {
                    imageApi.profileImage(info.uid ?: "", it)
                        .catch { e ->
                            e.message
                            fail.invoke()
                        }.collect()
                }
            } ?: deleteFile(info)

            userApi.setUserProfile(
                info.uid ?: "",
                nickName,
                profileImgPath,
                intro,
                imgFile != null
            )
                .catch { e ->
                    e.printStackTrace()
                    fail.invoke()
                }.collect { success(profileImgPath) }

        } ?: notUser.invoke()

    }

    private suspend fun deleteFile(userData: UserData) {
        if (userData.checkProfileImg) {
            withContext(Dispatchers.IO) {
                userData.profileImgPath?.let {
                    imageApi.removeProfileImg(it).catch { e ->
                        e.printStackTrace()
                    }.collect()
                }
            }
        }
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

    override suspend fun confirmUserMessage(uid: String) {
        userApi.confirmUserMessage(uid).collect()
    }

}