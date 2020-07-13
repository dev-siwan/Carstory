package com.like.drive.motorfeed

import android.app.Application
import com.google.firebase.FirebaseApp
import com.like.drive.motorfeed.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MotorFeedApplication :Application(){

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@MotorFeedApplication)
            modules(listOf(cacheModule, remoteModule,viewModelModule,repositoryModule,useCaseModule))
            logger(AndroidLogger(Level.DEBUG))
        }
    }

}