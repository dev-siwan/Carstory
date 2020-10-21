package com.like.drive.carstory.repository.user

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.like.drive.carstory.data.user.UserData
import java.io.File

interface UserRepository {
    suspend fun getUser(
        success: (UserData) -> Unit,
        fail: () -> Unit,
        emptyUser: () -> Unit
    )

    suspend fun getUserProfile(
        uid: String,
        success: (UserData?) -> Unit,
        fail: () -> Unit
    )

    suspend fun checkUser(): Boolean
    suspend fun loginCredential(
        authCredential: AuthCredential,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    )

    suspend fun createKaKaoCustomToken(
        uid: String,
        success: (String) -> Unit,
        error: () -> Unit
    )

    suspend fun loginCustomToken(
        token: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    )

    suspend fun setUser(userData: UserData, success: () -> Unit, fail: () -> Unit)
    suspend fun signEmail(
        email: String,
        password: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    )

    suspend fun loginEmail(
        email: String,
        password: String,
        success: (FirebaseUser) -> Unit,
        error: () -> Unit
    )

    suspend fun updateProfile(
        nickName: String,
        intro: String? = null,
        existNickName: () -> Unit,
        success: () -> Unit,
        fail: () -> Unit,
        notUser: () -> Unit
    )

    suspend fun signOut(success: () -> Unit, fail: () -> Unit)

    suspend fun confirmUserMessage(uid: String)
}