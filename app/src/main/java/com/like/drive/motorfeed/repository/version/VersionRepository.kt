package com.like.drive.motorfeed.repository.version

interface VersionRepository {
    suspend fun checkMotorTypeVersion(insertMotorType:()->Unit,passInsertMotorType:()->Unit,fail:()->Unit)
}