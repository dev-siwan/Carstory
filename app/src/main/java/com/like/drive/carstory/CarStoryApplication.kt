package com.like.drive.carstory

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp
import com.kakao.auth.KakaoSDK
import com.like.drive.carstory.common.define.FirebaseDefine
import com.like.drive.carstory.kakao.KakaoSDKAdapter
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CarStoryApplication : Application() {


    init {
        instance = this
    }

    companion object {
        private var instance: CarStoryApplication? = null

        fun getContext(): CarStoryApplication {
            return instance as CarStoryApplication
        }
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        initFacebookSDK()
        Timber.plant(Timber.DebugTree())
        initNotificationChannel()
        kakaoInit()

    }

    private fun initFacebookSDK() {
        AppEventsLogger.activateApp(this)
    }

    /**
     * 알림 채널 설정
     */
    private fun initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelMessage = NotificationChannel(
                FirebaseDefine.CHANNEL_ID,
                FirebaseDefine.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channelMessage.enableVibration(true)
            channelMessage.setShowBadge(false)
            channelMessage.vibrationPattern = longArrayOf(100, 200, 100, 200)
            channelMessage.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager.createNotificationChannel(channelMessage)
        }
    }

    private fun kakaoInit() {
        KakaoSDK.init(KakaoSDKAdapter())
    }

}