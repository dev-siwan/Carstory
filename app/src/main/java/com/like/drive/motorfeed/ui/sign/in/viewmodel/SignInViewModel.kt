package com.like.drive.motorfeed.ui.sign.`in`.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.google.firebase.auth.FacebookAuthProvider
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) :BaseViewModel(){

    val email = ObservableField<String>()
    val password =ObservableField<String>()
    val emailEvent = SingleLiveEvent<String>()

    val loginEmailClickEvent = SingleLiveEvent<Unit>()

     fun handleFacebookAccessToken(token: AccessToken) {

        val credential = FacebookAuthProvider.getCredential(token.token)
         viewModelScope.launch {
             userRepository.signFaceBook(credential,
                 success = {

                 },
                 error = {

                 })
         }
    }


}