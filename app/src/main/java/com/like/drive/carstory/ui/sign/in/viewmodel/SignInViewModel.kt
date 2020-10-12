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
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.main.activity.MainActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class SignInViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val email = ObservableField<String>()
    val password = ObservableField<String>()

    val loginFacebookClickEvent = SingleLiveEvent<Unit>()
    val kakaoSessionOpenFailed = SingleLiveEvent<Unit>()
    val kakaoSignUpError = SingleLiveEvent<String>()

    val isLoading = SingleLiveEvent<Boolean>()

    val completeEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<SignInErrorType>()
    val emptyNickNameEvent = SingleLiveEvent<Unit>()

    val moveToSignUpEvent = SingleLiveEvent<Unit>()

    //카카오 콜백
    private val kakaoCallback = object : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            kakaoSessionOpenFailed.call()
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
            userRepository.loginFaceBook(credential,
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
            userRepository.loginFaceBook(credential,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.FACEBOOK_ERROR)
                })
        }
    }

    fun initKakao() {
        kakaoSdkLogOut()

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
            }

            override fun onFailure(errorResult: ErrorResult?) {
                Timber.i("실패 메세지 =${errorResult?.errorMessage}")
            }

            override fun onSuccess(result: MeV2Response) {

                Session.getCurrentSession().tokenInfo.accessToken?.run {
                    createKaKaoToken(result.id.toString())
                } ?: kakaoSignUpError.call()

            }
        })
    }

    fun createKaKaoToken(accessToken: String) {
        viewModelScope.launch {
            userRepository.createKaKaoCustomToken(accessToken = accessToken,
                success = {
                    customLogin(it)
                },
                error = {})
        }
    }

    private fun kakaoSdkLogOut() =
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {}
        })

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

    private fun customLogin(token: String) {
        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginKaKaoToken(token,
                success = {
                    getUser(it)
                },
                error = {
                    setErrorEvent(SignInErrorType.KAKAO_ERROR)
                })
        }
    }

    fun checkEnable(email: String?, password: String?) =
        !email.isNullOrBlank() && !password.isNullOrBlank()

    override fun onCleared() {
        super.onCleared()
        Session.getCurrentSession().removeCallback(kakaoCallback)
    }
}

enum class SignInErrorType {
    USER_ERROR, USER_BAN, LOGIN_ERROR, FACEBOOK_ERROR, KAKAO_ERROR, GOOGLE_ERROR
}