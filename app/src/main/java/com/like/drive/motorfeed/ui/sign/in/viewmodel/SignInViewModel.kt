package com.like.drive.motorfeed.ui.sign.`in`.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) :BaseViewModel(){

    val email = ObservableField<String>()
    val password =ObservableField<String>()

    val loginFacebookClickEvent = SingleLiveEvent<Unit>()

    val isLoading = SingleLiveEvent<Boolean>()

    val completeEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<SignInErrorType>()

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

    fun loginEmail(){
        isLoading.value = true

        viewModelScope.launch {
            userRepository.loginEmail(email.get()!!,password.get()!!,
            success = {
                setUser(it)
            },
            error = {
                setErrorEvent(SignInErrorType.LOGIN_ERROR)
            })
        }
    }

    private fun setUser(user:FirebaseUser){
        viewModelScope.launch {
            userRepository.setUser(UserData(uid = user.uid,email = user.email),
            success = {
                completeEvent.call()
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
                    setCompleteEvent()
                }, fail = {
                    setErrorEvent(SignInErrorType.USER_ERROR)
                }, ban = {
                    setErrorEvent(SignInErrorType.USER_BAN)
                }, empty = {
                    setUser(user)
                }
            )
        }
    }


    private fun setErrorEvent(type:SignInErrorType){
        errorEvent.value = type
        isLoading.value = false
    }

    private fun setCompleteEvent(){
        completeEvent.call()
        isLoading.value = false
    }


    fun checkEnable(email:String?,password:String?) =
        !email.isNullOrBlank() && !password.isNullOrBlank()
}

enum class SignInErrorType{
    USER_ERROR,USER_BAN,LOGIN_ERROR,FACEBOOK_ERROR
}