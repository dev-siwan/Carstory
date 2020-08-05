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
    override suspend fun uploadImage(fid: String, imgFile: File): Flow<Uri?> {
          return  fireBaseTask.uploadImage(firebaseStorage.reference.child(CollectionName.FEED).child(fid).child(imgFile.name.replace(".","")), imgFile)
        }

    override suspend fun profileImage(uid: String, imgFile: File): Flow<Uri?> {
        return  fireBaseTask.uploadImage(firebaseStorage.reference.child(CollectionName.USER).child(uid).child("profileImg"), imgFile)
    }
}