package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.motor.MotorTypeRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<MotorTypeRepository> { MotorTypeRepositoryImpl(get(),get()) }
}