package com.like.drive.motorfeed.repository.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

class FeedRepositoryImpl(
    private val feedApi: FeedApi,
    private val imgApi: ImageApi,
    private val firestore: FirebaseFirestore
) : FeedRepository {

    private var successPhoto:(()->Unit?)? = null
    private var photoFileList = ArrayList<PhotoData>()
    private lateinit var documentID:String

    override suspend fun addFeed(
        feedData: FeedData,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int)->Unit,
        success :
    ) {
        this.photoFileList=photoFileList
        documentID = firestore.collection(CollectionName.FEED).document().id

        var photoCount = 0
        val isPhotoUpload = withContext(Dispatchers.IO){checkImgUpload()}
        if(isPhotoUpload){
            val imgList = this.photoFileList.map { it.imgUrl!! }.toList()

        }
        successPhoto={
            photoCount = photoCount.plus(1)
            photoSuccessCount.invoke(photoCount)
        }
    }



    private suspend fun checkImgUpload(): Boolean {
        if (photoFileList.any{ it.imgUrl == null }) {
            withContext(Dispatchers.IO) { imgUpload(documentID) }
            return false
        }
        return true
    }

    private suspend fun imgUpload(documentID: String) {
        photoFileList.filter { it.imgUrl == null }.forEach {
            imgApi.uploadImage(documentID, it.file!!).let { handler ->
                when (handler) {
                    is ResultState.Success -> photoFileList.add(PhotoData().apply {
                        this.imgUrl = handler.data.toString()
                        successPhoto?.invoke()
                        checkImgUpload()
                    })
                    is ResultState.Error -> photoFileList.add(PhotoData().apply {
                        this.imgUrl = null
                    })
                }
            }
        }
    }


}