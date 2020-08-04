package com.like.drive.motorfeed.repository.feed


import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class FeedRepositoryImpl(
    private val feedApi: FeedApi,
    private val imgApi: ImageApi,
    private val firestore: FirebaseFirestore
) : FeedRepository {

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

        if (photoFileList.isNotEmpty()) {
            checkImgUpload().collect{
                photoSuccessCount(it)
            }
        }

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

    override suspend fun getFeedComment(fid:String): Flow<List<CommentData>> {
       return feedApi.getComment(fid)
    }

    override suspend fun getFeed(fid: String): Flow<FeedData?> {
        return feedApi.getFeed(fid)
    }

    override suspend fun getFeedList(brandCode: Int?, modelCode: Int?): Flow<List<FeedData>> {
        return feedApi.getFeedList(brandCode,modelCode)
    }


    private suspend fun checkImgUpload(): Flow<Int> =
        flow {
            if (photoFileList.any { it.imgUrl == null }) {
                imgUpload().collect {
                    emit(it)
                }
            }
            emit(photoFileList.size)
        }

    private suspend fun imgUpload(): Flow<Int> = flow {
        photoFileList.forEachIndexed { index, photoData ->
            if (photoData.imgUrl == null) {
                imgApi.uploadImage(documentID, photoData.file!!)
                    .catch { photoFileList[index].imgUrl = null }
                    .collect {
                        photoFileList[index].imgUrl = it.toString()
                        emit(photoFileList.filter { photoData -> photoData.imgUrl != null }.size)
                        checkImgUpload()
                    }
            }
        }
    }

}