package com.like.drive.motorfeed.usecase.splash

import com.google.firebase.auth.FirebaseAuth
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import java.lang.Exception

class SplashUseCaseImpl(private val firebaseAuth: FirebaseAuth,
private val motorTypeRepository: MotorTypeRepository) :SplashUseCase{
    override fun isLogin(): Boolean = firebaseAuth.currentUser != null

    override suspend fun setMotorTypeList(): Boolean {
        return try {
            motorTypeRepository.setMotorTypeList()
            true
        } catch (e: Exception) {
            false
        }
    }
}