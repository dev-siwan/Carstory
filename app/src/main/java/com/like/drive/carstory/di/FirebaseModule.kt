package com.like.drive.carstory.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.like.drive.carstory.remote.task.FireBaseTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    private val fireStoreInstance by lazy { FirebaseFirestore.getInstance() }
    private val fireStorageInstance by lazy { FirebaseStorage.getInstance() }
    private val fireBaseTask by lazy { FireBaseTask() }
    private val fireBaseAuth by lazy { FirebaseAuth.getInstance() }

    @Singleton
    @Provides
    fun provideFireStore() = fireStoreInstance

    @Singleton
    @Provides
    fun provideFireStorage() = fireStorageInstance

    @Singleton
    @Provides
    fun provideFirebaseAuth() = fireBaseAuth

    @Singleton
    @Provides
    fun provideFirebaseTask() = fireBaseTask

}

