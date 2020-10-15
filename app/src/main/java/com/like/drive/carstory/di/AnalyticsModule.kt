package com.like.drive.carstory.di

import com.like.drive.carstory.analytics.AnalyticsEventLog
import org.koin.dsl.module

val analyticsModule = module {
    single { AnalyticsEventLog() }
}