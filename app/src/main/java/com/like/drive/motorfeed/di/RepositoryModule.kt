package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.motor.MotorTypeRepositoryImpl
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.repository.user.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MotorTypeRepository> { MotorTypeRepositoryImpl(get(),get()) }
    single<UserRepository> {UserRepositoryImpl(get())}
}