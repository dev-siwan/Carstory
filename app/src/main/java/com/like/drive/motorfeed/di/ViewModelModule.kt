package com.like.drive.motorfeed.di

import com.like.drive.motorfeed.ui.board.detail.viewmodel.BoardDetailViewModel
import com.like.drive.motorfeed.ui.board.list.viewmodel.BoardListViewModel
import com.like.drive.motorfeed.ui.board.upload.viewmodel.UploadViewModel
import com.like.drive.motorfeed.ui.filter.viewmodel.FilterViewModel
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.more.viewmodel.MoreViewModel
import com.like.drive.motorfeed.ui.motor.viewmodel.MotorTypeViewModel
import com.like.drive.motorfeed.ui.notice.detail.viewmodel.NoticeDetailViewModel
import com.like.drive.motorfeed.ui.notice.list.viewmodel.NoticeListViewModel
import com.like.drive.motorfeed.ui.notification.viewmodel.NotificationSettingViewModel
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
    viewModel { UploadViewModel(get()) }
    viewModel { BoardDetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { BoardListViewModel(get()) }
    viewModel { LargeThanViewModel() }
    viewModel { SearchViewModel() }
    viewModel { FilterViewModel() }
    viewModel { NotificationViewModel(get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { PasswordUpdateViewModel(get()) }
    viewModel { NotificationSettingViewModel() }
    viewModel { NoticeListViewModel(get()) }
    viewModel { NoticeDetailViewModel() }
}