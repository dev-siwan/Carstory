package com.like.drive.carstory.remote.api.img

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.like.drive.carstory.remote.common.FireBaseTask
import com.like.drive.carstory.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow
import java.io.File

class ImageApiImpl(
    private val firebaseStorage: FirebaseStorage,
    private val fireBaseTask: FireBaseTask
) : ImageApi {
    override suspend fun uploadBoardImage(bid: String, imgFile: File, index: Int): Flow<Uri?> {
        return fireBaseTask.uploadImage(
            firebaseStorage.reference.child(CollectionName.BOARD).child(bid).child("img$index"),
            imgFile
        )
    }

    override suspend fun profileImage(uid: String, imgFile: File): Flow<Boolean> {
        val ref  = firebaseStorage.reference.child(CollectionName.USER).child(uid).child("profileImg")
        return fireBaseTask.uploadProfileImage(
            ref,
            imgFile
        )
    }

    override suspend fun deleteBoardImage(bid: String, index: Int): Flow<Boolean> {
        return fireBaseTask.deleteImage(
            firebaseStorage.reference.child(CollectionName.BOARD).child(bid).child("img$index")
        )
    }
}