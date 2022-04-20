package com.like.drive.carstory.di

import com.like.drive.carstory.remote.api.admin.AdminApi
import com.like.drive.carstory.remote.api.admin.AdminApiImpl
import com.like.drive.carstory.remote.api.board.BoardApi
import com.like.drive.carstory.remote.api.board.BoardApiImpl
import com.like.drive.carstory.remote.api.img.ImageApi
import com.like.drive.carstory.remote.api.img.ImageApiImpl
import com.like.drive.carstory.remote.api.motor.MotorTypeApi
import com.like.drive.carstory.remote.api.motor.MotorTypeApiImpl
import com.like.drive.carstory.remote.api.notice.NoticeApiImpl
import com.like.drive.carstory.remote.api.notification.NotificationApi
import com.like.drive.carstory.remote.api.notification.NotificationApiImpl
import com.like.drive.carstory.remote.api.report.ReportApi
import com.like.drive.carstory.remote.api.report.ReportApiImpl
import com.like.drive.carstory.remote.api.user.UserApi
import com.like.drive.carstory.remote.api.user.UserApiImpl
import com.like.drive.carstory.remote.api.version.VersionApi
import com.like.drive.carstory.remote.api.version.VersionApiImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by ksw1012 on 2022/04/20.
 * Description:
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Singleton
    @Binds
    abstract fun bindVersionApi(
        versionApiImpl: VersionApiImpl
    ): VersionApi

    @Singleton
    @Binds
    abstract fun bindMotorTypeApi(
        motorTypeApiImpl: MotorTypeApiImpl
    ): MotorTypeApi

    @Singleton
    @Binds
    abstract fun bindUserApi(
        userApiImpl: UserApiImpl
    ): UserApi

    @Singleton
    @Binds
    abstract fun bindImageApi(
        imageApiImpl: ImageApiImpl
    ): ImageApi

    @Singleton
    @Binds
    abstract fun bindBoardApi(
        boardApiImpl: BoardApiImpl
    ): BoardApi

    @Singleton
    @Binds
    abstract fun bindNotificationApi(
        notificationApiImpl: NotificationApiImpl
    ): NotificationApi

    @Singleton
    @Binds
    abstract fun bindNoticeApi(
        noticeApiImpl: NoticeApiImpl
    ): NoticeApiImpl

    @Singleton
    @Binds
    abstract fun bindReportApi(
        reportApiImpl: ReportApiImpl
    ): ReportApi

    @Singleton
    @Binds
    abstract fun bindAdminApi(
        adminApiImpl: AdminApiImpl
    ): AdminApi
}