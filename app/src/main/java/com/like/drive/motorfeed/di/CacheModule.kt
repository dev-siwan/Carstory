package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.cache.AppDB
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val cacheModule = module {
    single { AppDB.getInstance(androidApplication()) }
    single { get<AppDB>().MotorTypeDao() }
}