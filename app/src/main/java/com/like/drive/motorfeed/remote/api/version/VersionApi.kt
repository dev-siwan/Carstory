package com.like.drive.motorfeed.remote.api.version

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.common.Version

interface VersionApi{
    suspend fun getMotorTypeVersion(): ResultState<Version>
}