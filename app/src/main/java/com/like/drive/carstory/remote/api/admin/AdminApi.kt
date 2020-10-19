package com.like.drive.carstory.remote.api.admin

import kotlinx.coroutines.flow.Flow

interface AdminApi {

    suspend fun sendUserMessage(
        uid: String,
        message: String
    ): Flow<Boolean>

    suspend fun setUserBan(
        uid: String,
        message: String
    ): Flow<Boolean>

}