package com.like.drive.motorfeed.usecase.splash

import com.google.firebase.auth.FirebaseAuth
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.repository.base.ResultRepository
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.user.UserRepository
import java.lang.Exception

class SplashUseCaseImpl(private val firebaseAuth: FirebaseAuth,
                        private val motorTypeRepository: MotorTypeRepository,
                        private val userRepository: UserRepository) :SplashUseCase{

    override suspend fun isLogin(): Boolean = firebaseAuth.currentUser != null

    override suspend fun setMotorTypeList(){
            motorTypeRepository.setMotorTypeList(error = {})
    }

    override suspend fun getUser(success: () -> Unit, fail: () -> Unit) {
        userRepository.getUser().let { resultState ->
            when (resultState) {
                is ResultState.Success -> {
                    UserInfo.userInfo = resultState.data
                    success.invoke()
                }
                is ResultState.Error -> {
                    fail.invoke()
                }
            }
        }
    }
}