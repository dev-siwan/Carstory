package com.like.drive.motorfeed.remote.api.user

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.user.UserData

interface UserApi {
    suspend fun getUser(): ResultState<UserData>
    suspend fun checkUser():Boolean
}