package com.like.drive.carstory.di

import com.like.drive.carstory.repository.admin.AdminRepository
import com.like.drive.carstory.repository.admin.AdminRepositoryImpl
import com.like.drive.carstory.repository.board.BoardRepository
import com.like.drive.carstory.repository.board.BoardRepositoryImpl
import com.like.drive.carstory.repository.motor.MotorTypeRepository
import com.like.drive.carstory.repository.motor.MotorTypeRepositoryImpl
import com.like.drive.carstory.repository.notice.NoticeRepository
import com.like.drive.carstory.repository.notice.NoticeRepositoryImpl
import com.like.drive.carstory.repository.notification.NotificationRepository
import com.like.drive.carstory.repository.notification.NotificationRepositoryImpl
import com.like.drive.carstory.repository.report.ReportRepository
import com.like.drive.carstory.repository.report.ReportRepositoryImpl
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.repository.user.UserRepositoryImpl
import com.like.drive.carstory.repository.version.VersionRepository
import com.like.drive.carstory.repository.version.VersionRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindVersionRepository(
        versionRepositoryImpl: VersionRepositoryImpl
    ): VersionRepository

    @Singleton
    @Binds
    abstract fun bindMotorTypeRepository(
        motorTypeRepositoryImpl: MotorTypeRepositoryImpl
    ): MotorTypeRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindBoardRepository(
        boardRepositoryImpl: BoardRepositoryImpl
    ): BoardRepository

    @Singleton
    @Binds
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Singleton
    @Binds
    abstract fun bindNoticeRepository(
        noticeRepositoryImpl: NoticeRepositoryImpl
    ): NoticeRepository

    @Singleton
    @Binds
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl
    ): ReportRepository

    @Singleton
    @Binds
    abstract fun bindAdminRepository(
        adminRepositoryImpl: AdminRepositoryImpl
    ): AdminRepository
}
