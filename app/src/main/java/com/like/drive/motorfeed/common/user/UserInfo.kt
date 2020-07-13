package com.like.drive.motorfeed.common.user

import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.UserPref
import org.koin.core.KoinComponent
import org.koin.core.inject

object UserInfo : KoinComponent {

   private val userPref:UserPref by inject()

    var userInfo:UserData?
    get()= userPref.userData?.run { this } ?: UserData()
    set(value) {userPref.userData = value}

}