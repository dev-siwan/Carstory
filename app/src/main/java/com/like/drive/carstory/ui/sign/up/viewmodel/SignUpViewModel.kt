package com.like.drive.carstory.ui.sign.up.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.valid.FieldValidEnum
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.base.ext.isEmail
import com.like.drive.carstory.ui.base.ext.isPassword
import com.like.drive.carstory.ui.base.ext.isPasswordValid
import kotlinx.coroutines.launch

class SignUpViewModel(private val userRepository: UserRepository) : BaseViewModel() {
    val email = ObservableField<String>()
    val password = ObservableField<String>()
    val passwordValid = ObservableField<String>()

    val completeEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<Unit>()

    val fieldWarning = SingleLiveEvent<FieldValidEnum>()

    val isLoading = SingleLiveEvent<Boolean>()

    private fun validCheck(): Boolean {
        when {
            !email.get()!!.isEmail() -> setFieldValid(FieldValidEnum.EMAIL)
            !password.get()!!.isPassword() -> setFieldValid(FieldValidEnum.PASSWORD)
            !passwordValid.get()!!
                .isPasswordValid(password.get()!!) -> setFieldValid(FieldValidEnum.PASSWORD_VALID)
        }
        return true
    }

    fun doSignEmail() {

        isLoading.value = true

        if (validCheck()) {
            val email = email.get()!!
            val password = password.get()!!
            viewModelScope.launch {
                userRepository.signEmail(email = email, password = password,
                    success = {
                        setEmail(it)
                    },
                    error = {
                        setError()
                    })
            }
        }
    }

    private fun setEmail(user: FirebaseUser) {
        viewModelScope.launch {
            userRepository.setUser(UserData(uid = user.uid, email = user.email),
                success = {
                    setComplete()
                },
                fail = {
                    setError()
                })
        }
    }

    private fun setComplete() {
        completeEvent.call()
        isLoading.value = false
    }

    private fun setError() {
        errorEvent.call()
        isLoading.value = false
    }

    private fun setFieldValid(type: FieldValidEnum): Boolean {
        fieldWarning.value = type
        return false
    }

    fun checkEnable(email: String?, password: String?, passwordValid: String?) =
        !email.isNullOrBlank() && !password.isNullOrBlank() && !passwordValid.isNullOrBlank()
}