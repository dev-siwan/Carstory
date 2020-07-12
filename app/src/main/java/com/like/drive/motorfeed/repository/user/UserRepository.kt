package com.like.drive.motorfeed.repository.user

import com.like.drive.motorfeed.common.ResultState
import com.like.drive.motorfeed.data.user.UserData

interface UserRepository{
    suspend fun getUser():ResultState<UserData>
}