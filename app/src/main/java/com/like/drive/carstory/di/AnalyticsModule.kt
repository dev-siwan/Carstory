package com.like.drive.carstory.di

import com.like.drive.carstory.analytics.AnalyticsEventLog
import com.like.drive.carstory.util.ad.NativeAdUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Singleton
    @Provides
    fun provideAnalyticsEventLog() = AnalyticsEventLog()

    @Singleton
    @Provides
    fun provideANativeAdUtil() = NativeAdUtil()
}
