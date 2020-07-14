package com.like.drive.motorfeed.repository.user

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.remote.api.user.UserApi

class UserRepositoryImpl(private val userApi: UserApi) :UserRepository{

    override suspend fun getUser(): ResultState<UserData> {
        return userApi.getUser()
    }

    override suspend fun checkUser()= userApi.checkUser()

}