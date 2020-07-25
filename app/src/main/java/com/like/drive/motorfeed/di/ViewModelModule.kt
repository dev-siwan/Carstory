package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.motor.viewmodel.MotorTypeViewModel
import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.motorfeed.ui.sign.up.viewmodel.SignUpViewModel
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(),get(),get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MotorTypeViewModel(get()) }
}