package com.like.drive.motorfeed.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApi
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApiImpl
import com.like.drive.motorfeed.remote.api.user.UserApi
import com.like.drive.motorfeed.remote.api.user.UserApiImpl
import com.like.drive.motorfeed.remote.api.version.VersionApi
import com.like.drive.motorfeed.remote.api.version.VersionApiImpl
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.repository.version.VersionRepositoryImpl
import org.koin.dsl.module

val remoteModule= module {
    val fireStoreInstance by lazy { FirebaseFirestore.getInstance() }
    val fireStorageInstance by lazy { FirebaseStorage.getInstance() }
    val fireBaseTask by lazy { FireBaseTask() }
    val fireBaseAuth by lazy{ FirebaseAuth.getInstance() }

    single { fireStoreInstance }
    single { fireBaseTask }
    single { fireStorageInstance }
    single { fireBaseAuth }

    /**
     * Api
     * **/
    single<VersionApi> { VersionApiImpl(get(),get()) }
    single<MotorTypeApi> { MotorTypeApiImpl(get(),get()) }
    single<UserApi> { UserApiImpl(get(),get(),get()) }
}