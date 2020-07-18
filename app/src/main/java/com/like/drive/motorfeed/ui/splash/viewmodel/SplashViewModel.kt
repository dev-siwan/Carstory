package com.like.drive.motorfeed.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.repository.version.VersionRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(
    private val versionRepository: VersionRepository,
    private val motorTypeRepository: MotorTypeRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val errorEvent = SingleLiveEvent<SplashErrorType>()
    val completeEvent = SingleLiveEvent<SplashCompleteType>()

    init {
        versionCheck()
    }


    private fun versionCheck() {
        viewModelScope.launch {
            versionRepository.checkMotorTypeVersion(
                insertMotorType = {
                    setMotorType()
                },
                passInsertMotorType = {
                    isNotEmptyMotorTypeList{result->
                        if(result) checkUser() else setMotorType()
                    }
                },
                fail = {
                    setErrorEvent(SplashErrorType.VERSION_CHECK_ERROR)
                })
        }
    }

    private fun isNotEmptyMotorTypeList(isNotEmpty:(Boolean)->Unit) {
        viewModelScope.launch {
            isNotEmpty(motorTypeRepository.isNotEmptyMotorTypeList())
        }
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
                                setErrorEvent(SplashErrorType.USER_EMPTY)
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
   VERSION_CHECK_ERROR,USER_ERROR,MOTOR_TYPE_ERROR,USER_EMPTY
}
enum class SplashCompleteType{
   LOGIN , FEED
}