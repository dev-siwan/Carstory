package com.like.drive.carstory.common.user

import android.app.NotificationManager
import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.like.drive.carstory.CarStoryApplication
import com.like.drive.carstory.common.define.FirebaseDefine
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.pref.UserPref
import com.like.drive.carstory.remote.api.user.UserApi
import com.like.drive.carstory.repository.board.BoardRepository
import com.like.drive.carstory.repository.notification.NotificationRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserInfo @Inject constructor(
    private val userPref: UserPref,
    private val userApi: UserApi,
    private val notificationRepository: NotificationRepository,
    private val boardRepository: BoardRepository
) {

    var userInfo: UserData? = null
    var isNoticeTopic: Boolean = true

    init {

        userInfo?.let { userPref.userData }
        isNoticeTopic = userPref.isNoticeTopic.also {
            updateNoticeSubScribe(it)
        }

    }

    fun signOut() {
        CoroutineScope(Dispatchers.IO).launch {
            userApi.signOut()
            userPref.removeUserInfo()
            notificationRepository.deleteAll()
            boardRepository.removeAllLike()
            (CarStoryApplication.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancelAll()
            disableFcm()
            userInfo = null
            cancel()
        }
    }

    private fun disableFcm() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        FirebaseInstanceId.getInstance()
            .deleteToken(FirebaseInstanceId.getInstance().id, FirebaseMessaging.INSTANCE_ID_SCOPE)
    }

    fun updateFcm(token: String?) {

        CoroutineScope(Dispatchers.IO).launch {
            FirebaseMessaging.getInstance().isAutoInitEnabled = true
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

    fun setUserData(userData: UserData) {
        userInfo = userData
        userPref.userData = userData
    }

    fun updateProfile(nickName: String, intro: String?) {
        userInfo?.apply {
            this.nickName = nickName
            this.intro = intro
        }
    }

    fun updateNoticeSubScribe(isSubScribe: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (isSubScribe) {
                FirebaseMessaging.getInstance().subscribeToTopic(FirebaseDefine.TOPIC_NOTICE)
                    .await()
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(FirebaseDefine.TOPIC_NOTICE)
                    .await()
            }
            cancel()
        }

        isNoticeTopic = isSubScribe
    }

    fun updateCommentSubScribe(isSubScribe: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            userApi.updateCommentSubscribe(isSubScribe)
                .catch { error -> error.printStackTrace() }
                .collect {
                    userInfo?.commentSubscribe = isSubScribe
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