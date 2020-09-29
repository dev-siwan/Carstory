package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.repository.board.BoardRepository
import com.like.drive.motorfeed.repository.board.BoardRepositoryImpl
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.motor.MotorTypeRepositoryImpl
import com.like.drive.motorfeed.repository.notice.NoticeRepository
import com.like.drive.motorfeed.repository.notice.NoticeRepositoryImpl
import com.like.drive.motorfeed.repository.notification.NotificationRepository
import com.like.drive.motorfeed.repository.notification.NotificationRepositoryImpl
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.repository.user.UserRepositoryImpl
import com.like.drive.motorfeed.repository.version.VersionRepository
import com.like.drive.motorfeed.repository.version.VersionRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<VersionRepository> { VersionRepositoryImpl(get()) }
    single<MotorTypeRepository> { MotorTypeRepositoryImpl(get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<BoardRepository> { BoardRepositoryImpl(get(), get(), get(), get(),get(),get()) }
    single<NotificationRepository> { NotificationRepositoryImpl(get()) }
    single<NoticeRepository> { NoticeRepositoryImpl(get(),get()) }
}