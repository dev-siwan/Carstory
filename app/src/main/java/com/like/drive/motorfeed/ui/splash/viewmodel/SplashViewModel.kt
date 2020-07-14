package com.like.drive.motorfeed.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(
    private val motorTypeRepository: MotorTypeRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val errorEvent = SingleLiveEvent<SplashErrorType>()
    val completeEvent = SingleLiveEvent<SplashCompleteType>()

    init {
        setMotorType()
    }

    private fun setMotorType() {
        viewModelScope.launch {
            motorTypeRepository.setMotorTypeList({
                checkUser()
            }, {
                setErrorEvent(SplashErrorType.MOTOR_TYPE_ERROR)
            })
        }
    }

    private fun checkUser() {
        viewModelScope.launch {
            if (userRepository.checkUser()) {

                userRepository.getUser().let {resultState ->
                    when(resultState){
                        is ResultState.Success -> {
                            UserInfo.userInfo = resultState.data
                            setCompleteEvent(SplashCompleteType.FEED)
                        }
                        is ResultState.Error -> {
                            resultState.emptyDocument?.let {
                                setErrorEvent(SplashErrorType.USER_BLANK)
                            }
                            resultState.exception?.let {
                                setErrorEvent(SplashErrorType.USER_ERROR)
                            }
                        }

                    }
                }

            } else {
                setCompleteEvent(SplashCompleteType.LOGIN)
            }
        }
    }

    private fun setErrorEvent(type:SplashErrorType){
        errorEvent.value = type
    }

    private fun setCompleteEvent(type:SplashCompleteType){
        completeEvent.value = type
    }

}

enum class SplashErrorType{
   USER_ERROR,MOTOR_TYPE_ERROR,USER_BLANK
}
enum class SplashCompleteType{
   LOGIN , FEED
}