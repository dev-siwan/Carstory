package com.like.drive.motorfeed.ui.sign.password.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.valid.FieldValidEnum
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.base.ext.isPassword
import com.like.drive.motorfeed.ui.base.ext.isPasswordValid
import kotlinx.coroutines.launch

class PasswordUpdateViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val password = ObservableField<String>()
    val rePassword = ObservableField<String>()
    val rePasswordValid = ObservableField<String>()

    val fieldWarning = SingleLiveEvent<FieldValidEnum>()

    val isLoading = SingleLiveEvent<Boolean>()

    val completedEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<@StringRes Int>()

    fun updatePassword() {
        if (validCheck()) {

            isLoading.value = true

            viewModelScope.launch {
                userRepository.updatePassword(
                    password.get()!!,
                    rePassword.get()!!,
                    success = {
                        completedEvent.call()
                        isLoading.value = false
                    },
                    failCredential = {
                        errorEvent.value = R.string.password_credential_fail
                        isLoading.value = false
                    },
                    fail = {
                        errorEvent.value = R.string.password_error_dialog_desc
                        isLoading.value = false
                    })
            }
        }

    }

    private fun validCheck(): Boolean {
        return when {
            !rePassword.get()!!.isPassword() -> setFieldValid(FieldValidEnum.PASSWORD)
            !rePasswordValid.get()!!
                .isPasswordValid(rePassword.get()!!) -> setFieldValid(FieldValidEnum.PASSWORD_VALID)
            else -> true
        }
    }

    private fun setFieldValid(type: FieldValidEnum): Boolean {
        fieldWarning.value = type
        return false
    }

}