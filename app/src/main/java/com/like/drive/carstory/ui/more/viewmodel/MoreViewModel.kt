package com.like.drive.carstory.ui.more.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.repository.user.UserRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MoreViewModel(private val userRepository: UserRepository) : BaseViewModel() {

    val resetPasswordCompleteEvent = SingleLiveEvent<Unit>()
    val errorEvent = SingleLiveEvent<@StringRes Int>()

    private val _userInfo = MutableLiveData<UserData>()
    val userInfo: LiveData<UserData> get() = _userInfo

    init {
        UserInfo.userInfo?.let { _userInfo.value = it }
    }

    fun resetPassword() {
        _userInfo.value?.email?.let {
            viewModelScope.launch {
                userRepository.resetPassword(it, {
                    resetPasswordCompleteEvent.call()
                }, {
                    errorEventListener(R.string.reset_email_fail_message)
                })
            }
        } ?: errorEventListener(R.string.reset_email_fail_message)
    }

    private fun errorEventListener(resID: Int) {
        errorEvent.value = resID
    }
}