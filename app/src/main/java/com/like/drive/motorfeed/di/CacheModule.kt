package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.cache.AppDB
import com.like.drive.motorfeed.pref.UserPref
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val cacheModule = module {
    /**
     * DB Init
     * **/
    single { AppDB.getInstance(androidApplication()) }

    /**
     * DAO
     * **/
    single { get<AppDB>().motorTypeDao() }
    single { get<AppDB>().notificationDao() }

    /**
     * Preference
     * **/

    factory { UserPref(androidApplication()) }
}