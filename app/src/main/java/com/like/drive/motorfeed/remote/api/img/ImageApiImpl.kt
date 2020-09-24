package com.like.drive.motorfeed.remote.api.img

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow
import java.io.File

class ImageApiImpl(
    private val firebaseStorage: FirebaseStorage,
    private val fireBaseTask: FireBaseTask
) : ImageApi {
    override suspend fun uploadFeedImage(fid: String, imgFile: File, index: Int): Flow<Uri?> {
        return fireBaseTask.uploadImage(
            firebaseStorage.reference.child(CollectionName.BOARD).child(fid).child("img$index"),
            imgFile
        )
    }

    override suspend fun profileImage(uid: String, imgFile: File): Flow<String?> {
        return fireBaseTask.uploadProfileImage(
            firebaseStorage.reference.child(CollectionName.USER).child(uid).child("profileImg"),
            imgFile
        )
    }

    override suspend fun deleteFeedImage(fid: String, index: Int): Flow<Boolean> {
        return fireBaseTask.deleteImage(
            firebaseStorage.reference.child(CollectionName.BOARD).child(fid).child("img$index")
        )
    }
}