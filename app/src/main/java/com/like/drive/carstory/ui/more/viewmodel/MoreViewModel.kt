package com.like.drive.carstory.ui.more.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.ui.base.BaseViewModel

class MoreViewModel : BaseViewModel() {

    private val _userInfo = MutableLiveData<UserData>()
    val userInfo: LiveData<UserData> get() = _userInfo

    init {
        getUserInfo()
    }

    fun getUserInfo(){
        UserInfo.userInfo?.let { _userInfo.value = it }
    }
}