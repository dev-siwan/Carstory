package com.like.drive.carstory.ui.more.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(userInfo: UserInfo) : BaseViewModel() {

    val userInfoData = MutableLiveData<UserData>(userInfo.userInfo)

}