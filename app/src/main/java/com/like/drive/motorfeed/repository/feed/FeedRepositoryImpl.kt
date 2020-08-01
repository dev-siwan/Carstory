package com.like.drive.motorfeed.repository.feed


import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.ui.upload.data.FeedUploadField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class FeedRepositoryImpl(
    private val feedApi: FeedApi,
    private val imgApi: ImageApi,
    private val firestore: FirebaseFirestore
) : FeedRepository {

    private var successPhoto: ((Int) -> Unit?)? = null
    private var photoFileList = ArrayList<PhotoData>()
    private lateinit var documentID: String

    override suspend fun addFeed(
        feedField: FeedUploadField,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int) -> Unit,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    ) {

        this.photoFileList = photoFileList
        documentID = firestore.collection(CollectionName.FEED).document().id

        checkImgUpload()
        successPhoto = { photoSuccessCount.invoke(it) }

        val creteFeedData = FeedData().createData(
            fid = documentID,
            feedUploadField = feedField,
            motorTypeData = feedField.motorTypeData,
            imgList = photoFileList
        )

        feedApi.addFeed(creteFeedData)
            .catch {
                fail.invoke()
            }
            .collect { isComplete ->
                if (isComplete) {
                    success(creteFeedData)
                } else {
                    fail.invoke()
                }
            }
    }


    private suspend fun checkImgUpload() = withContext(Dispatchers.IO) {
        if (photoFileList.any { it.imgUrl == null }) {
            imgUpload(documentID)
        }
        return@withContext
    }

    private suspend fun imgUpload(documentID: String) {
        photoFileList.forEachIndexed { index, photoData ->
            if (photoData.imgUrl == null) {
                imgApi.uploadImage(documentID, photoData.file!!)
                    .catch { photoFileList[index].imgUrl = null }
                    .collect {
                        photoFileList[index].imgUrl = it.toString()
                        successPhoto?.invoke(photoFileList.filter { photoData -> photoData.imgUrl != null }.size)
                        checkImgUpload()
                    }
            }
        }
    }

}