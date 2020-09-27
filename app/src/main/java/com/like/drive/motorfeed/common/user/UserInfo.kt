package com.like.drive.motorfeed.common.user

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.like.drive.motorfeed.common.define.FirebaseDefine
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.remote.api.user.UserApi
import com.like.drive.motorfeed.repository.board.BoardRepository
import com.like.drive.motorfeed.repository.notification.NotificationRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import org.koin.core.KoinComponent
import org.koin.core.inject

object UserInfo : KoinComponent {
    val userPref: UserPref by inject()
    private val userApi: UserApi by inject()
    private val notificationRepo: NotificationRepository by inject()
    private val boardRepository: BoardRepository by inject()

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
            notificationRepo.allDelete()
            boardRepository.removeAllLike()
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

    fun setUserData(userData: UserData) {
        userInfo = userData
        userPref.userData = userData
    }

    fun updateProfile(nickName: String, intro: String?, imgPath: String?) {
        userInfo?.apply {
            this.nickName = nickName
            this.intro = intro
            imgPath?.let {
                this.profileImgPath = it
            }

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