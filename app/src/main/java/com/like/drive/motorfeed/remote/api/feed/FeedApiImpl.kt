package com.like.drive.motorfeed.remote.api.feed

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.like.drive.motorfeed.common.define.FunctionDefine
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentFunData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.remote.reference.CollectionName.FEED_COMMENT
import com.like.drive.motorfeed.remote.reference.CollectionName.FEED_RE_COMMENT
import com.like.drive.motorfeed.ui.feed.data.FeedCountEnum
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import kotlinx.coroutines.flow.Flow
import java.util.*

class FeedApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : FeedApi {
    override suspend fun setFeed(feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.setData(
            fireStore.collection(CollectionName.FEED).document(feedData.fid ?: ""), feedData
        )
    }

    override suspend fun setUserFeed(uid: String, feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.setData(
            fireStore.collection(CollectionName.USER).document(uid).collection(CollectionName.FEED)
                .document(feedData.fid ?: ""), feedData
        )
    }

    override suspend fun removeFeed(feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.delete(
            fireStore.collection(CollectionName.FEED).document(feedData.fid ?: "")
        )
    }

    override suspend fun removeUserFeed(feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.delete(
            fireStore.collection(CollectionName.USER).document(feedData.userInfo?.uid ?: "")
                .collection(CollectionName.FEED)
                .document(feedData.fid ?: "")
        )
    }

    override suspend fun getComment(fid: String): Flow<List<CommentData>> {
        val collection =
            fireStore.collection(CollectionName.FEED).document(fid).collection(FEED_COMMENT)
        return fireBaseTask.getData(collection, CommentData::class.java)
    }

    override suspend fun getReComment(fid: String): Flow<List<ReCommentData>> {
        val collection =
            fireStore.collection(CollectionName.FEED).document(fid).collection(FEED_RE_COMMENT)
        return fireBaseTask.getData(collection, ReCommentData::class.java)
    }

    override suspend fun getFeed(fid: String): Flow<FeedData?> {
        val document = fireStore.collection(CollectionName.FEED).document(fid)
        return fireBaseTask.getData(document, FeedData::class.java)
    }

    override suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData?,
        feedTypeData: FeedTypeData?,
        tagStr: String?
    ): Flow<List<FeedData>> {
        val feedCollection = fireStore.collection(CollectionName.FEED)

        val query = when {
            motorTypeData != null && feedTypeData == null -> {
                if (motorTypeData.modelCode == 0) {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                } else {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                        .whereEqualTo(
                            MODE_CODE_FIELD, motorTypeData.modelCode
                        )
                }
            }

            feedTypeData != null && motorTypeData == null -> {
                feedCollection.whereEqualTo(FEED_TYPE_CODE_FIELD, feedTypeData.typeCode)
            }

            motorTypeData != null && feedTypeData != null -> {
                if (motorTypeData.modelCode == 0) {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                        .whereEqualTo(FEED_TYPE_CODE_FIELD, feedTypeData.typeCode)
                } else {
                    feedCollection.whereEqualTo(BRAND_CODE_FIELD, motorTypeData.brandCode)
                        .whereEqualTo(
                            MODE_CODE_FIELD, motorTypeData.modelCode
                        ).whereEqualTo(FEED_TYPE_CODE_FIELD, feedTypeData.typeCode)
                }
            }
            else -> {
                feedCollection
            }
        }

        val tagQuery = tagStr?.let {
            query
                .whereArrayContains(FEED_TAG_LIST, tagStr)
        } ?: query

        return fireBaseTask.getData(
            tagQuery.whereLessThan(CREATE_DATE_FIELD, date)
                .orderBy(CREATE_DATE_FIELD, Query.Direction.DESCENDING).limit(INIT_SIZE.toLong()),
            FeedData::class.java
        )
    }

    override suspend fun addComment(commentData: CommentData): Flow<Boolean> {
        val commentFeedCollection =
            fireStore.collection(CollectionName.FEED).document(commentData.fid ?: "")
                .collection(FEED_COMMENT)

        val cid = commentFeedCollection.document().id

        commentData.cid = cid

        return fireBaseTask.setData(commentFeedCollection.document(cid), commentData)
    }

    override suspend fun setFuncComment(commentFunData: CommentFunData): Flow<String> {
        return fireBaseTask.setFunction(commentFunData.getHashMap(), FunctionDefine.ADD_COMMENT)
    }

    override suspend fun updateCount(fid: String, flag: FeedCountEnum) {
        val document = fireStore.collection(CollectionName.FEED).document(fid)
        when (flag) {
            FeedCountEnum.ADD_COMMENT -> {
                document.update(COMMENT_COUNT_FIELD, FieldValue.increment(1))
            }
            FeedCountEnum.DELETE_COMMENT -> {
                document.update(COMMENT_COUNT_FIELD, FieldValue.increment(-1))
            }
            FeedCountEnum.LIKE -> {
                document.update(LIKE_COUNT_FIELD, FieldValue.increment(1))
            }
            FeedCountEnum.UNLIKE -> {
                document.update(LIKE_COUNT_FIELD, FieldValue.increment(-1))
            }
            FeedCountEnum.VIEW -> {
                document.update(VIEW_COUNT_FIELD, FieldValue.increment(1))
            }
        }

    }

    override suspend fun addReComment(reCommentData: ReCommentData): Flow<Boolean> {
        val reCommentFeedCollection =
            fireStore.collection(CollectionName.FEED).document(reCommentData.fid ?: "")
                .collection(FEED_RE_COMMENT)

        val rcId = reCommentFeedCollection.document().id
        reCommentData.rcId = rcId

        return fireBaseTask.setData(reCommentFeedCollection.document(rcId), reCommentData)
    }

    override suspend fun updateComment(commentData: CommentData): Flow<Boolean> {
        val document =
            fireStore.collection(CollectionName.FEED).document(commentData.fid ?: "")
                .collection(FEED_COMMENT)
                .document(commentData.cid ?: "")
        return fireBaseTask.setData(document, commentData)
    }

    override suspend fun updateReComment(
        reCommentData: ReCommentData
    ): Flow<Boolean> {
        val document =
            fireStore.collection(CollectionName.FEED).document(reCommentData.fid ?: "")
                .collection(FEED_RE_COMMENT)
                .document(reCommentData.cid ?: "")
        return fireBaseTask.setData(document, reCommentData)
    }

    override suspend fun removeComment(commentData: CommentData): Flow<Boolean> {
        val document = fireStore.collection(CollectionName.FEED).document(commentData.fid ?: "")
            .collection(FEED_COMMENT).document(commentData.cid ?: "")

        return fireBaseTask.delete(document)
    }

    override suspend fun removeReComment(reCommentData: ReCommentData): Flow<Boolean> {
        val document =
            fireStore.collection(CollectionName.FEED).document(reCommentData.fid ?: "").collection(
                FEED_RE_COMMENT
            ).document(reCommentData.rcId ?: "")
        return fireBaseTask.delete(document)
    }

    companion object {
        const val COMMENT_COUNT_FIELD = "commentCount"
        const val LIKE_COUNT_FIELD = "likeCount"
        const val VIEW_COUNT_FIELD = "viewCount"
        const val BRAND_CODE_FIELD = "brandCode"
        const val MODE_CODE_FIELD = "modelCode"
        const val FEED_TYPE_CODE_FIELD = "feedTypeCode"
        const val CREATE_DATE_FIELD = "createDate"
        const val FEED_TAG_LIST = "feedTagList"
        const val INIT_SIZE = 3
    }

}