package com.like.drive.carstory.repository.version

interface VersionRepository {
    suspend fun checkMotorTypeVersion(insertMotorType:()->Unit,passInsertMotorType:()->Unit,fail:()->Unit)
}