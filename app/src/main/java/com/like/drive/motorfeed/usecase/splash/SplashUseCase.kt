package com.like.drive.motorfeed.usecase.splash

interface SplashUseCase{
    fun isLogin():Boolean
    suspend fun setMotorTypeList():Boolean
}