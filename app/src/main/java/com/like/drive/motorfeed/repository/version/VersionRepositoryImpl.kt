package com.like.drive.motorfeed.repository.version


import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.remote.api.version.VersionApi

class VersionRepositoryImpl(private val versionApi: VersionApi) : VersionRepository {
    override suspend fun checkMotorTypeVersion(
        insertMotorType: () -> Unit,
        passInsertMotorType: () -> Unit,
        fail: () -> Unit
    ) {
        versionApi.getMotorTypeVersion().let { result ->
            when (result) {
                is ResultState.Success -> {
                    result.data.version?.let {
                        if (UserInfo.userPref.motorTypeVersion ?: 0 < it) {
                            UserInfo.userPref.motorTypeVersion = it
                            insertMotorType.invoke()
                        } else {
                            passInsertMotorType.invoke()
                        }
                    } ?: fail.invoke()

                }
                is ResultState.Error -> fail.invoke()
            }
        }
    }
}