package com.like.drive.motorfeed.remote.api.img

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageApiImpl(
    private val firebaseStorage: FirebaseStorage,
    private val fireBaseTask: FireBaseTask
) : ImageApi {
    override suspend fun uploadImage(fid: String, imgFile: File): ResultState<Uri> {
          return  fireBaseTask.uploadImage(firebaseStorage.reference.child(CollectionName.FEED).child(fid), imgFile)
        }
    }