package com.like.drive.motorfeed.common.user

import com.google.firebase.auth.FirebaseAuth
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.remote.api.user.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

object UserInfo : KoinComponent {
    val userPref: UserPref by inject()
    private val userApi: UserApi by inject()

    var userInfo: UserData?
        get() = userPref.userData?.run { this } ?: UserData()
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

    fun updateFcm() {
        CoroutineScope(Dispatchers.IO).launch {
            if (userPref.isNewToken) {
                userPref.fcmToken?.let {
                    userApi.updateFcmToken(it).catch {error->
                        error.printStackTrace()
                    }.collect { isSuccess ->
                        if (isSuccess) {
                            userPref.isNewToken = false
                        }
                    }
                }
            }
            cancel()
        }
    }

}