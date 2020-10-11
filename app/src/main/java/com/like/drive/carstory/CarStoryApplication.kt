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
import com.like.drive.carstory.di.*
import com.like.drive.carstory.kakao.KakaoSDKAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

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
        initKoin()
        initNotificationChannel()
        kakaoInit()

    }

    private fun initFacebookSDK() {
        AppEventsLogger.activateApp(this)
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@CarStoryApplication)
            modules(
                listOf(
                    cacheModule,
                    remoteModule,
                    viewModelModule,
                    repositoryModule,
                    networkModule
                )
            )
            logger(AndroidLogger(Level.DEBUG))
        }
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

    private fun kakaoInit(){
        KakaoSDK.init(KakaoSDKAdapter())
    }

}