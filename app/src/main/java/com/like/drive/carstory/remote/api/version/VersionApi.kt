package com.like.drive.carstory.remote.api.version

import com.like.drive.carstory.data.common.Version
import kotlinx.coroutines.flow.Flow

interface VersionApi{
    suspend fun getMotorTypeVersion(): Flow<Version?>
}