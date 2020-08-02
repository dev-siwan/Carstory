package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.repository.feed.FeedRepositoryImpl
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.motor.MotorTypeRepositoryImpl
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.repository.user.UserRepositoryImpl
import com.like.drive.motorfeed.repository.version.VersionRepository
import com.like.drive.motorfeed.repository.version.VersionRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<VersionRepository>{VersionRepositoryImpl(get())}
    single<MotorTypeRepository> { MotorTypeRepositoryImpl(get(),get()) }
    single<UserRepository> {UserRepositoryImpl(get())}
    single<FeedRepository>{FeedRepositoryImpl(get(),get(),get(),get())}
}