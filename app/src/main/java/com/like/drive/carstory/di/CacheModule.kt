package com.like.drive.carstory.di

import com.like.drive.carstory.cache.AppDB
import com.like.drive.carstory.pref.UserPref
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
    single { get<AppDB>().likeDao() }

    /**
     * Preference
     * **/

    single { UserPref(androidApplication()) }
}