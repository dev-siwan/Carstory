package com.like.drive.motorfeed.remote.api.img

import com.google.firebase.storage.FirebaseStorage
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ImageApiImpl(
    private val firebaseStorage: FirebaseStorage,
    private val fireBaseTask: FireBaseTask
) : ImageApi {

    override suspend fun uploadImageList(fid: String, imgFile: List<File>): List<String?> {
        val imgUrlList = ArrayList<String?>()
        imgFile.forEach {
            fireBaseTask.uploadImage(
                firebaseStorage.reference.child(CollectionName.FEED).child(fid), it
            ).let { handler ->
                when (handler) {
                    is ResultState.Success -> {
                        imgUrlList.add(handler.data.toString())
                    }
                    is ResultState.Error -> {
                        handler.exception?.let { imgUrlList.add(null) }
                    }
                }
            }
        }
        return imgUrlList
    }
}