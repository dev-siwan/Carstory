package com.like.drive.carstory.di

import android.content.Context
import com.like.drive.carstory.cache.AppDB
import com.like.drive.carstory.pref.UserPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Provides
    @Singleton
    fun provideUserPref(@ApplicationContext context: Context) = UserPref(context)

    @Provides
    @Singleton
    fun provideAppDB(@ApplicationContext context: Context) = AppDB.getInstance(context)

    @Provides
    @Singleton
    fun provideMotorTypeDao(appDB: AppDB) = appDB.motorTypeDao()

    @Provides
    @Singleton
    fun notificationDao(appDB: AppDB) = appDB.notificationDao()

    @Provides
    @Singleton
    fun likeDao(appDB: AppDB) = appDB.likeDao()

}
