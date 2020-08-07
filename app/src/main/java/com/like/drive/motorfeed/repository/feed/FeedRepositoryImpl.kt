package com.like.drive.motorfeed.repository.feed


import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.ui.feed.data.FeedCountEnum
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.coroutines.flow.*

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
            checkImgUpload().collect {
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
            .zip(feedApi.addUserFeed(UserInfo.userInfo?.uid ?: "", creteFeedData)) { t1, t2 ->

                if (t1 && t2) {
                    success(creteFeedData)
                } else {
                    fail.invoke()
                }
            }
            .catch {
                fail.invoke()
            }
            .collect()
    }

    override suspend fun setLike(fid: String,isUp:Boolean) {
        if(isUp) feedApi.updateCount(fid, FeedCountEnum.LIKE) else feedApi.updateCount(fid, FeedCountEnum.UNLIKE)
    }

    override suspend fun getFeedComment(fid: String): Flow<List<CommentData>> {
        return feedApi.getComment(fid)
    }

    override suspend fun getFeed(
        fid: String,
        success: (FeedData?, List<CommentData>?) -> Unit,
        fail: () -> Unit
    ) {
        feedApi.getFeed(fid).zip(feedApi.getComment(fid)) { feedData, commentList ->
            success.invoke(feedData, commentList)
            if (feedData?.uid != UserInfo.userInfo?.uid) {
                feedApi.updateCount(fid, FeedCountEnum.VIEW)
            }
        }.catch {
            fail.invoke()
        }.collect()
    }

    override suspend fun getFeedList(brandCode: Int?, modelCode: Int?): Flow<List<FeedData>> {
        return feedApi.getFeedList(brandCode, modelCode)
    }

    override suspend fun addComment(
        fid: String,
        comment: String,
        success: (CommentData) -> Unit,
        fail: () -> Unit
    ) {
        val commentData = CommentData().createComment(fid = fid, commentStr = comment)

        feedApi.addComment(commentData).catch { e ->
            e.printStackTrace()
            fail.invoke()
        }
            .collect {
                success(commentData)
                feedApi.updateCount(fid, FeedCountEnum.ADD_COMMENT)
            }
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