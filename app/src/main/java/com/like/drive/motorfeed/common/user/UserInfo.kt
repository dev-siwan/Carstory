package com.like.drive.motorfeed.common.user

import com.google.firebase.iid.FirebaseInstanceId
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.remote.api.user.UserApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import org.koin.core.KoinComponent
import org.koin.core.inject

object UserInfo : KoinComponent {
    val userPref: UserPref by inject()
    private val userApi: UserApi by inject()

    var userInfo: UserData?
        get() = userPref.userData?.run { this }
        set(value) {
            userPref.userData = value
        }

    fun signOut() {
        CoroutineScope(Dispatchers.IO).launch {
            userApi.signOut()
            userPref.removeUserInfo()
            userInfo = null
            cancel()
        }
    }

    fun updateFcm(token: String?) {

        CoroutineScope(Dispatchers.IO).launch {

            val newToken = getToken()
            if (token != newToken) {
                userPref.fcmToken?.let {
                    userApi.updateFcmToken(it).catch { error ->
                        error.printStackTrace()
                    }.collect {
                        userPref.fcmToken = newToken
                    }
                }
            }
            cancel()
        }
    }

    private suspend fun getToken(): String? {

        if (userPref.fcmToken == null) {
            withContext(Dispatchers.IO) {
                userPref.fcmToken = FirebaseInstanceId.getInstance().instanceId.await().token
            }
        }

        return userPref.fcmToken
    }

}