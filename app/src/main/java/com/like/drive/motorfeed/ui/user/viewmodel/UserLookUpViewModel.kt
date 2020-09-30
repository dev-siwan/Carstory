package com.like.drive.motorfeed.ui.user.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.ui.base.BaseViewModel

class UserLookUpViewModel() : BaseViewModel() {

    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> get() = _userData

    fun init(userData: UserData) {
        _userData.value = userData
    }


}