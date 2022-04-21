package com.like.drive.carstory.di

import android.content.Context
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.pref.UserPref
import com.like.drive.carstory.remote.api.user.UserApi
import com.like.drive.carstory.repository.board.BoardRepository
import com.like.drive.carstory.repository.notification.NotificationRepository
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

object DataModule {

    @Provides
    @Singleton
    fun provideUserData(
        userPref: UserPref,
        notificationRepository: NotificationRepository,
        userApi: UserApi,
        boardRepository: BoardRepository
    ) = UserInfo(
        userPref = userPref,
        notificationRepository = notificationRepository,
        userApi = userApi,
        boardRepository = boardRepository
    )

    @Provides
    @Singleton
    fun provideUserPref(@ApplicationContext context: Context) = UserPref(context)

}