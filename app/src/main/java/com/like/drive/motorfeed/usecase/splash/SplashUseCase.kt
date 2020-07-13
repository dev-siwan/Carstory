package com.like.drive.motorfeed.usecase.splash

import android.service.autofill.UserData
import com.like.drive.motorfeed.common.async.ResultState

interface SplashUseCase{
    suspend fun isLogin():Boolean
    suspend fun setMotorTypeList()
    suspend fun getUser(success:()->Unit , fail:()->Unit)
}