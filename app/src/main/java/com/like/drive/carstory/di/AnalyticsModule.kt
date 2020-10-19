package com.like.drive.carstory.di

import com.like.drive.carstory.analytics.AnalyticsEventLog
import com.like.drive.carstory.util.ad.NativeAdUtil
import org.koin.dsl.module

val analyticsModule = module {
    single { AnalyticsEventLog() }
    factory { NativeAdUtil() }
}