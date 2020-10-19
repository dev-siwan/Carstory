package com.like.drive.carstory.repository.admin

import com.like.drive.carstory.remote.api.admin.AdminApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class AdminRepositoryImpl(private val adminApi: AdminApi) : AdminRepository {
    override suspend fun sendUserMessage(
        uid: String,
        message: String,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        adminApi.sendUserMessage(uid, message).catch { e ->
            e.message
            fail.invoke()
        }.collect {
            success.invoke()
        }
    }

    override suspend fun setUserBam(
        uid: String,
        message: String,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        adminApi.setUserBan(uid, message).catch { e ->
            e.message
            fail.invoke()
        }.collect {
            success.invoke()
        }
    }

}