package com.like.drive.carstory.ui.sign.`in`.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val email = ObservableField<String>()
    val password = ObservableField<String>()

    val loginFacebookClickEvent = SingleLiveEvent<Unit>()

    val isLoading = SingleLiveEvent<Boolean>()

    val completeEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<SignInErrorType>()
    val emptyNickNameEvent = SingleLiveEvent<Unit>()

    val moveToSignUpEvent = SingleLiveEvent<Unit>()

    /**
     * 페이스북 토큰 핸들러
     * @param accessToken 을 이용하여
     * @param credential 값에 AuthCredential 생성
     * */
    fun handleFacebookAccessToken(accessToken: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(accessToken.token)

        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginFaceBook(credential,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.FACEBOOK_ERROR)
                })
        }
    }

    fun createKaKaoToken(accessToken: String) {
        viewModelScope.launch {
            userRepository.createKaKaoCustomToken(accessToken = accessToken,
                success = {
                    Timber.i("accessToken = $it")
                },
                error = {})
        }
    }

    fun loginEmail() {
        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginEmail(email.get()!!, password.get()!!,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.LOGIN_ERROR)
                })
        }
    }

    private fun saveUser(user: FirebaseUser) {
        viewModelScope.launch {
            userRepository.setUser(UserData(
                uid = user.uid,
                email = user.email,
                emailSignUp = false
            ),
                success = {
                    emptyNickNameEvent.call()
                },
                fail = {
                    setErrorEvent(SignInErrorType.USER_ERROR)
                })
        }
    }

    private fun getUser(user: FirebaseUser) {
        viewModelScope.launch {
            userRepository.getUser(
                success = {
                    successUser()
                }, fail = {
                    setErrorEvent(SignInErrorType.USER_ERROR)
                }, userBan = {
                    setErrorEvent(SignInErrorType.USER_BAN)
                }, emptyUser = {
                    saveUser(user)
                }
            )
        }
    }

    private fun setErrorEvent(type: SignInErrorType) {
        errorEvent.value = type
        isLoading.value = false
    }

    private fun successUser() {
        UserInfo.userInfo?.nickName?.let {
            completeEvent.call()
        } ?: emptyNickNameEvent.call()
        isLoading.value = false
    }

    fun checkEnable(email: String?, password: String?) =
        !email.isNullOrBlank() && !password.isNullOrBlank()
}

enum class SignInErrorType {
    USER_ERROR, USER_BAN, LOGIN_ERROR, FACEBOOK_ERROR
}