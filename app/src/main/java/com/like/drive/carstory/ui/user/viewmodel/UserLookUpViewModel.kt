package com.like.drive.carstory.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.ui.base.BaseViewModel

class UserLookUpViewModel() : BaseViewModel() {

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> get() = _userData

    fun init(userData: UserData) {
        _userData.value = userData
    }

}