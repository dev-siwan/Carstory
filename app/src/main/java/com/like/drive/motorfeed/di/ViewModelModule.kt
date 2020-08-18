package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.tag.viewmodel.FeedTagViewModel
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.motor.viewmodel.MotorTypeViewModel
import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.motorfeed.ui.sign.up.viewmodel.SignUpViewModel
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel
import com.like.drive.motorfeed.ui.profile.viewmodel.ProfileViewModel
import com.like.drive.motorfeed.ui.view.large.viewmodel.LargeThanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(),get(),get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { MotorTypeViewModel(get()) }
    viewModel { GalleryViewModel()}
    viewModel { FeedUploadViewModel(get()) }
    viewModel { FeedDetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { FeedListViewModel(get()) }
    viewModel { FeedTagViewModel() }
    viewModel { LargeThanViewModel() }
}