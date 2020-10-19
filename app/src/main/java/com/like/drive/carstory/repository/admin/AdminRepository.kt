package com.like.drive.carstory.repository.admin

interface AdminRepository {

    suspend fun sendUserMessage(
        uid: String,
        message: String,
        success: () -> Unit,
        fail: () -> Unit
    )

    suspend fun setUserBam(
        uid: String,
        message: String,
        success: () -> Unit,
        fail: () -> Unit
    )

}