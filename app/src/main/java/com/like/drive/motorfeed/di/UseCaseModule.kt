package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.usecase.splash.SplashUseCase
import com.like.drive.motorfeed.usecase.splash.SplashUseCaseImpl
import org.koin.dsl.module

val useCaseModule = module {
    single<SplashUseCase> { SplashUseCaseImpl(get(),get()) }
}