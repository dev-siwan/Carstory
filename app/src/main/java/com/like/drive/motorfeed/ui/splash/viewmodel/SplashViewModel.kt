package com.like.drive.motorfeed.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.usecase.splash.SplashUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(private val splashUseCase: SplashUseCase) : BaseViewModel() {


    fun init(){
        viewModelScope.launch {
            splashUseCase.setMotorTypeList()
            splashUseCase.isLogin().checkLogin()
            }
        }

    fun Boolean.checkLogin(){

    }

}