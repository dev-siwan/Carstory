package com.like.drive.motorfeed.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.feed.FeedApiImpl
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.api.img.ImageApiImpl
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApi
import com.like.drive.motorfeed.remote.api.motor.MotorTypeApiImpl
import com.like.drive.motorfeed.remote.api.notice.NoticeApi
import com.like.drive.motorfeed.remote.api.notice.NoticeApiImpl
import com.like.drive.motorfeed.remote.api.notification.NotificationApi
import com.like.drive.motorfeed.remote.api.notification.NotificationApiImpl
import com.like.drive.motorfeed.remote.api.user.UserApi
import com.like.drive.motorfeed.remote.api.user.UserApiImpl
import com.like.drive.motorfeed.remote.api.version.VersionApi
import com.like.drive.motorfeed.remote.api.version.VersionApiImpl
import com.like.drive.motorfeed.remote.common.FireBaseTask
import org.koin.dsl.module

val remoteModule = module {
    val fireStoreInstance by lazy { FirebaseFirestore.getInstance() }
    val fireStorageInstance by lazy { FirebaseStorage.getInstance() }
    val fireBaseTask by lazy { FireBaseTask() }
    val fireBaseAuth by lazy { FirebaseAuth.getInstance() }

    single { fireStoreInstance }
    single { fireStorageInstance }
    single { fireBaseAuth }
    factory { fireBaseTask }

    /**
     * Api
     * **/
    single<VersionApi> { VersionApiImpl(get(), get()) }
    single<MotorTypeApi> { MotorTypeApiImpl(get(), get()) }
    single<UserApi> { UserApiImpl(get(), get(), get()) }
    single<ImageApi> { ImageApiImpl(get(), get()) }
    single<FeedApi> { FeedApiImpl(get(), get()) }
    single<NotificationApi> { NotificationApiImpl(get()) }
    single<NoticeApi> { NoticeApiImpl(get(), get()) }

}