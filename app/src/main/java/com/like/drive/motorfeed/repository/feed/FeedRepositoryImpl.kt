package com.like.drive.motorfeed.repository.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctionsException
import com.like.drive.motorfeed.cache.dao.like.LikeDao
import com.like.drive.motorfeed.cache.entity.LikeEntity
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.notification.NotificationSendData
import com.like.drive.motorfeed.data.notification.NotificationType
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.api.notification.NotificationApi
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.ui.feed.data.FeedCountEnum
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class FeedRepositoryImpl(
    private val feedApi: FeedApi,
    private val imgApi: ImageApi,
    private val firestore: FirebaseFirestore,
    private val notificationApi: NotificationApi,
    private val likeDao: LikeDao
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
            imgList = photoFileList,
            feedTagList = feedField.feedTagList
        )

        feedApi.setFeed(creteFeedData)
            .zip(feedApi.setUserFeed(creteFeedData.userInfo?.uid ?: "", creteFeedData)) { t1, t2 ->
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

    override suspend fun updateFeed(
        feedField: FeedUploadField,
        feedData: FeedData,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    ) {
        val updateFeedData = FeedData().updateData(
            feedUploadField = feedField,
            motorTypeData = feedField.motorTypeData,
            feedData = feedData,
            feedTagList = feedField.feedTagList
        )

        feedApi.setFeed(updateFeedData)
            .zip(
                feedApi.setUserFeed(
                    updateFeedData.userInfo?.uid ?: "",
                    updateFeedData
                )
            ) { t1, t2 ->
                if (t1 && t2) {
                    success(updateFeedData)
                } else {
                    fail.invoke()
                }
            }
            .catch {
                fail.invoke()
            }
            .collect()
    }

    override suspend fun removeFeed(
        feedData: FeedData,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    ) {

        if (feedData.imageUrls?.isNotEmpty() == true) {
            feedData.imageUrls.forEachIndexed { index, _ ->
                imgApi.deleteFeedImage(feedData.fid ?: "", index).catch { fail.invoke() }.collect()
            }
        }

        feedApi.removeFeed(feedData).zip(feedApi.removeUserFeed(feedData)) { t1, t2 ->
            if (t1 && t2) {
                success(feedData)
            } else {
                fail.invoke()
            }
        }.catch {
            fail.invoke()
        }.collect()

    }

    override suspend fun removeUserFeed(
        feedData: FeedData
    ) {
        feedApi.removeUserFeed(feedData).catch { Unit }.collect()
    }

    override suspend fun setLike(fid: String, isUp: Boolean) {
        if (isUp) {
            feedApi.updateCount(fid, FeedCountEnum.LIKE)
            likeDao.insertFid(LikeEntity(fid = fid))
        } else {
            likeDao.deleteFid(fid)
            feedApi.updateCount(fid, FeedCountEnum.UNLIKE)
        }
    }

    override suspend fun getFeedComment(fid: String): Flow<List<CommentData>> {
        return feedApi.getComment(fid)
    }

    override suspend fun getFeed(
        fid: String,
        success: (FeedData?, List<CommentWrapData>?) -> Unit,
        isLike: (Boolean) -> Unit,
        fail: () -> Unit
    ) {
        feedApi.getFeed(fid)
            .zip(feedApi.getComment(fid).zip(feedApi.getReComment(fid)) { comment, reComment ->

                val list = mutableListOf<CommentWrapData>()

                comment.forEach {
                    val reCommentList =
                        reComment.filter { reCommentData -> reCommentData.cid == it.cid }
                            .toMutableList()

                    reCommentList.sortBy { item -> item.createDate }

                    list.add(CommentWrapData(commentData = it, reCommentList = reCommentList))
                }

                list.sortBy { it.commentData.createDate }

                return@zip list
            }) { feedData, commentWrapList ->

                feedData?.fid?.let {
                    isLike(isLikeFeed(fid))
                }

                success.invoke(feedData, commentWrapList)
            }.catch {
                fail.invoke()
            }.collect()
    }

    override suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData?,
        feedTypeData: FeedTypeData?,
        tagStr: String?
    ): Flow<List<FeedData>> =
        feedApi.getFeedList(date, motorTypeData, feedTypeData, tagStr)

    override suspend fun getUserFeedList(date: Date, uid: String): Flow<List<FeedData>> =
        feedApi.getUserFeedList(date, uid)

    override suspend fun addComment(
        feedData: FeedData,
        comment: String,
        success: (CommentData) -> Unit,
        fail: () -> Unit
    ) {
        val commentData =
            CommentData().createComment(fid = feedData.fid ?: "", commentStr = comment)

        feedApi.addComment(commentData).catch { e ->
            e.printStackTrace()
            fail.invoke()
        }.collect {
            success(commentData)

            if (feedData.userInfo?.uid != UserInfo.userInfo?.uid) {
                val data = NotificationSendData(
                    notificationType = NotificationType.COMMENT.value,
                    uid = feedData.userInfo?.uid,
                    fid = feedData.fid,
                    title = feedData.title,
                    message = comment
                )
                notificationApi.sendNotification(data).catch { e ->
                    if (e is FirebaseFunctionsException) {
                        e.message?.let { Timber.i(it) }
                    }
                }.collect()
            }
        }
    }

    override suspend fun addReComment(
        feedData: FeedData,
        commentData: CommentData,
        reComment: String,
        success: (ReCommentData) -> Unit,
        fail: () -> Unit
    ) {
        val reCommentData =
            ReCommentData().createComment(
                fid = feedData.fid ?: "",
                cid = commentData.cid ?: "",
                commentStr = reComment
            )

        feedApi.addReComment(reCommentData).catch {
            fail.invoke()
        }.collect {
            success(reCommentData)

            if (commentData.userInfo?.uid != UserInfo.userInfo?.uid) {
                val data = NotificationSendData(
                    notificationType = NotificationType.RE_COMMENT.value,
                    uid = commentData.userInfo?.uid,
                    fid = feedData.fid,
                    title = commentData.commentStr,
                    message = reComment
                )
                notificationApi.sendNotification(data).catch { e ->
                    if (e is FirebaseFunctionsException) {
                        e.message?.let { Timber.i(it) }
                    }
                }.collect()
            }
        }
    }

    override suspend fun updateComment(
        comment: CommentData,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        feedApi.updateComment(comment).catch {
            fail()
        }.collect {
            success()
        }
    }

    override suspend fun updateReComment(
        reCommentData: ReCommentData,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        feedApi.updateReComment(reCommentData).catch {
            fail()
        }.collect {
            success()
        }
    }

    override suspend fun removeComment(
        comment: CommentData,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        feedApi.removeComment(comment).catch {
            fail()
        }.collect {
            success()
        }
    }

    override suspend fun removeReComment(
        reCommentData: ReCommentData,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        feedApi.removeReComment(reCommentData).catch {
            fail()
        }.collect {
            success()
        }
    }

    override suspend fun isLikeFeed(fid: String): Boolean {
        return likeDao.isFeedEmpty(fid).isNotEmpty()
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
                imgApi.uploadFeedImage(documentID, photoData.file!!, index)
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