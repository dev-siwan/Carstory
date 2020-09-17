package com.like.drive.motorfeed.ui.sign.password.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.valid.FieldValidEnum
import com.like.drive.motorfeed.repository.user.UserRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.base.ext.isPassword
import com.like.drive.motorfeed.ui.base.ext.isPasswordValid
import kotlinx.coroutines.launch

class PasswordUpdateViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val password = ObservableField<String>()
    val passwordValid = ObservableField<String>()

    val fieldWarning = SingleLiveEvent<FieldValidEnum>()

    val isLoading = SingleLiveEvent<Boolean>()

    val completedEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<Unit>()

    fun updatePassword() {
        if (validCheck() == true) {

            isLoading.value = true

            viewModelScope.launch {
                userRepository.updatePassword(password.get()!!, {
                    completedEvent.call()
                    isLoading.value = false
                }, {
                    errorEvent.call()
                    isLoading.value = false
                })
            }
        }

    }

    private fun validCheck(): Boolean {
        return when {
            !password.get()!!.isPassword() -> setFieldValid(FieldValidEnum.PASSWORD)
            !passwordValid.get()!!
                .isPasswordValid(password.get()!!) -> setFieldValid(FieldValidEnum.PASSWORD_VALID)
            else -> true
        }
    }

    private fun setFieldValid(type: FieldValidEnum): Boolean {
        fieldWarning.value = type
        return false
    }

}