package com.like.drive.carstory.ui.sign.`in`.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
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

    val userBanComplete = SingleLiveEvent<UserData>()

    //카카오 콜백
    private val kakaoCallback = object : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            setErrorEvent(SignInErrorType.KAKAO_ERROR)
        }

        override fun onSessionOpened() {
            requestMe()
        }
    }

    /**
     * 페이스북 토큰 핸들러
     * @param accessToken 을 이용하여
     * @param credential 값에 AuthCredential 생성
     * */
    fun handleFacebookAccessToken(accessToken: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(accessToken.token)

        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginCredential(credential,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.FACEBOOK_ERROR)
                })
        }
    }

    fun handleGoogleAccessToken(idToken: String) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)

        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginCredential(credential,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.FACEBOOK_ERROR)
                })
        }
    }

    fun loginKaKao(activity: Activity) {
        Session.getCurrentSession().run {
            addCallback(kakaoCallback)
            open(AuthType.KAKAO_LOGIN_ALL, activity)
        }

    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean =
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)

    private fun requestMe() {

        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSessionClosed(errorResult: ErrorResult) {
                Timber.i("에러 메세지 =${errorResult.errorMessage}")
                setErrorEvent(SignInErrorType.KAKAO_ERROR)
            }

            override fun onFailure(errorResult: ErrorResult?) {
                Timber.i("실패 메세지 =${errorResult?.errorMessage}")
                setErrorEvent(SignInErrorType.KAKAO_ERROR)
            }

            override fun onSuccess(result: MeV2Response) {

                Session.getCurrentSession().tokenInfo.accessToken?.run {
                    createKaKaoToken(result.id.toString())
                } ?: setErrorEvent(SignInErrorType.KAKAO_ERROR)

            }
        })
    }

    fun createKaKaoToken(uid: String) {
        viewModelScope.launch {
            userRepository.createKaKaoCustomToken(uid = uid,
                success = {
                    customLogin(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.KAKAO_ERROR)
                })
        }
    }

    private fun saveUser(user: FirebaseUser) {
        viewModelScope.launch {
            userRepository.setUser(UserData(
                uid = user.uid,
                email = user.email
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
                    if (it.userBan) {
                        userBanComplete.value = it
                        isLoading.value = false
                    } else {
                        successUser()
                    }
                }, fail = {
                    setErrorEvent(SignInErrorType.USER_ERROR)
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

    private fun customLogin(token: String) {
        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginCustomToken(token,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.KAKAO_ERROR)
                })
        }
    }

    override fun onCleared() {
        super.onCleared()
        Session.getCurrentSession().removeCallback(kakaoCallback)
    }
}

enum class SignInErrorType {
    USER_ERROR, LOGIN_ERROR, FACEBOOK_ERROR, KAKAO_ERROR, GOOGLE_ERROR
}