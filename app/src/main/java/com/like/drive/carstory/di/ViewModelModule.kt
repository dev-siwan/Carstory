package com.like.drive.carstory.di

import com.like.drive.carstory.ui.board.detail.viewmodel.BoardDetailViewModel
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel
import com.like.drive.carstory.ui.board.upload.viewmodel.UploadViewModel
import com.like.drive.carstory.ui.filter.viewmodel.FilterViewModel
import com.like.drive.carstory.ui.gallery.viewmodel.GalleryViewModel
import com.like.drive.carstory.ui.home.viewmodel.HomeViewModel
import com.like.drive.carstory.ui.main.viewmodel.MainViewModel
import com.like.drive.carstory.ui.more.viewmodel.MoreViewModel
import com.like.drive.carstory.ui.motor.viewmodel.MotorTypeViewModel
import com.like.drive.carstory.ui.notice.detail.viewmodel.NoticeDetailViewModel
import com.like.drive.carstory.ui.notice.list.viewmodel.NoticeListViewModel
import com.like.drive.carstory.ui.notification.viewmodel.NotificationSettingViewModel
import com.like.drive.carstory.ui.notification.viewmodel.NotificationViewModel
import com.like.drive.carstory.ui.profile.viewmodel.ProfileViewModel
import com.like.drive.carstory.ui.report.list.viewmodel.ReportViewModel
import com.like.drive.carstory.ui.report.reg.viewmodel.ReportRegisterViewModel
import com.like.drive.carstory.ui.search.viewmodel.SearchViewModel
import com.like.drive.carstory.ui.sign.`in`.viewmodel.SignInViewModel
import com.like.drive.carstory.ui.sign.password.viewmodel.PasswordUpdateViewModel
import com.like.drive.carstory.ui.sign.up.viewmodel.SignUpViewModel
import com.like.drive.carstory.ui.splash.viewmodel.SplashViewModel
import com.like.drive.carstory.ui.user.viewmodel.UserLookUpViewModel
import com.like.drive.carstory.ui.view.large.viewmodel.LargeThanViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SplashViewModel(get(), get(), get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { MotorTypeViewModel(get()) }
    viewModel { GalleryViewModel() }
    viewModel { UploadViewModel(get()) }
    viewModel { BoardDetailViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { BoardListViewModel(get()) }
    viewModel { LargeThanViewModel() }
    viewModel { SearchViewModel(get()) }
    viewModel { FilterViewModel() }
    viewModel { NotificationViewModel(get()) }
    viewModel { MoreViewModel(get()) }
    viewModel { PasswordUpdateViewModel(get()) }
    viewModel { NotificationSettingViewModel() }
    viewModel { NoticeListViewModel(get()) }
    viewModel { NoticeDetailViewModel(get()) }
    viewModel { ReportRegisterViewModel() }
    viewModel { ReportViewModel(get()) }
    viewModel { UserLookUpViewModel() }
}