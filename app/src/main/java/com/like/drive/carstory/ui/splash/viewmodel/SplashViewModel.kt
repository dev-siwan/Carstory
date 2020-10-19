package com.like.drive.carstory.ui.splash.viewmodel

import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.motor.MotorTypeRepository
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.repository.version.VersionRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(
    private val versionRepository: VersionRepository,
    private val motorTypeRepository: MotorTypeRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val errorEvent = SingleLiveEvent<SplashErrorType>()
    val completeEvent = SingleLiveEvent<SplashCompleteType>()
    val emptyNickNameEvent = SingleLiveEvent<Unit>()

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
                    isNotEmptyMotorTypeList { result ->
                        if (result) checkUser() else setMotorType()
                    }
                },
                fail = {
                    setErrorEvent(SplashErrorType.VERSION_CHECK_ERROR)
                })
        }
    }

    private fun isNotEmptyMotorTypeList(isNotEmpty: (Boolean) -> Unit) {
        viewModelScope.launch {
            isNotEmpty(motorTypeRepository.isNotEmptyMotorTypeList())
        }
    }

    private fun setMotorType() {
        viewModelScope.launch {
            motorTypeRepository.setMotorTypeList(
                success = {
                    checkUser()
                },
                error = {
                    setErrorEvent(SplashErrorType.MOTOR_TYPE_ERROR)
                })
        }
    }

    private fun checkUser() {
        viewModelScope.launch {
            if (userRepository.checkUser()) {

                userRepository.getUser(
                    success = {
                        if (it.userBan) {
                            setErrorEvent(SplashErrorType.USER_BAN)
                            UserInfo.signOut()
                        } else {
                            successUser()
                        }
                    }, fail = {
                        setErrorEvent(SplashErrorType.USER_ERROR)
                    }, emptyUser = {
                        setErrorEvent(SplashErrorType.USER_EMPTY)
                    }
                )
            } else {
                setCompleteEvent(SplashCompleteType.LOGIN)
            }
        }
    }

    private fun setErrorEvent(type: SplashErrorType) {
        errorEvent.postValue(type)
    }

    private fun setCompleteEvent(type: SplashCompleteType) {
        completeEvent.postValue(type)
    }

    private fun successUser() {
        UserInfo.userInfo?.nickName?.let {
            setCompleteEvent(SplashCompleteType.HOME)
        } ?: emptyNickNameEvent.call()
    }

}

enum class SplashErrorType {
    VERSION_CHECK_ERROR, USER_ERROR, MOTOR_TYPE_ERROR, USER_EMPTY, USER_BAN
}

enum class SplashCompleteType {
    LOGIN, HOME
}