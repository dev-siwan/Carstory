package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel
import com.like.drive.motorfeed.ui.filter.viewmodel.FilterViewModel
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.home.viewmodel.NewsFeedViewModel
import com.like.drive.motorfeed.ui.home.viewmodel.UserFilterViewModel
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.more.viewmodel.MoreViewModel
import com.like.drive.motorfeed.ui.motor.viewmodel.MotorTypeViewModel
import com.like.drive.motorfeed.ui.notification.viewmodel.NotificationViewModel
import com.like.drive.motorfeed.ui.profile.viewmodel.ProfileViewModel
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import com.like.drive.motorfeed.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.motorfeed.ui.sign.password.viewmodel.PasswordUpdateViewModel
import com.like.drive.motorfeed.ui.sign.up.viewmodel.SignUpViewModel
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import com.like.drive.motorfeed.ui.view.large.viewmodel.LargeThanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { MotorTypeViewModel(get()) }
    viewModel { GalleryViewModel() }
    viewModel { FeedUploadViewModel(get()) }
    viewModel { FeedDetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { FeedListViewModel(get()) }
    viewModel { LargeThanViewModel() }
    viewModel { SearchViewModel() }
    viewModel { FilterViewModel() }
    viewModel { UserFilterViewModel() }
    viewModel { NewsFeedViewModel() }
    viewModel { NotificationViewModel(get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { PasswordUpdateViewModel(get()) }
}