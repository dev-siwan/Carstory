package com.like.drive.motorfeed.repository.user

import com.like.drive.motorfeed.common.ResultState
import com.like.drive.motorfeed.data.user.UserData

class UserRepositoryImpl :UserRepository{
    override suspend fun getUser(): ResultState<UserData> {
        TODO("Not yet implemented")
    }

}