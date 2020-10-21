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
import org.koin.dsl.module

val repositoryModule = module {
    single<VersionRepository> { VersionRepositoryImpl(get()) }
    single<MotorTypeRepository> { MotorTypeRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<BoardRepository> { BoardRepositoryImpl(get(), get(), get(), get(), get(), get(), get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
    single<NoticeRepository> { NoticeRepositoryImpl(get(), get()) }
    single<ReportRepository> { ReportRepositoryImpl(get()) }
    single<AdminRepository> { AdminRepositoryImpl(get()) }
}