package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(),get(),get()) }
    viewModel { SignInViewModel(get()) }
}