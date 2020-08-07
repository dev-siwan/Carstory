package com.like.drive.motorfeed.remote.api.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.remote.reference.CollectionName.FEED_COMMENT
import com.google.firebase.firestore.FieldValue
import com.like.drive.motorfeed.ui.feed.data.FeedCountEnum
import kotlinx.coroutines.flow.Flow

class FeedApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : FeedApi {
    override suspend fun addFeed(feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.setData(
            fireStore.collection(CollectionName.FEED).document(feedData.fid ?: ""), feedData
        )
    }

    override suspend fun addUserFeed(uid: String, feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.setData(
            fireStore.collection(CollectionName.USER).document(uid).collection(CollectionName.FEED)
                .document(feedData.fid ?: ""), feedData
        )
    }

    override suspend fun getComment(fid: String): Flow<List<CommentData>> {
        val collection =
            fireStore.collection(CollectionName.FEED).document(fid).collection(FEED_COMMENT)
        return fireBaseTask.getData(collection, CommentData::class.java)
    }

    override suspend fun getFeed(fid: String): Flow<FeedData?> {
        val document = fireStore.collection(CollectionName.FEED).document(fid)
        return fireBaseTask.getData(document, FeedData::class.java)
    }

    override suspend fun getFeedList(brandCode: Int?, modelCode: Int?): Flow<List<FeedData>> {
        val feedCollection = fireStore.collection(CollectionName.FEED)
        return fireBaseTask.getData(feedCollection, FeedData::class.java)
    }

    override suspend fun addComment(commentData: CommentData): Flow<Boolean> {
        val commentFeedCollection =
            fireStore.collection(CollectionName.FEED).document(commentData.fid ?: "")
                .collection(FEED_COMMENT)
        val cid = commentFeedCollection.document().id
        commentData.cid = cid

        return fireBaseTask.setData(commentFeedCollection, commentData)
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

    companion object {
        const val COMMENT_COUNT_FIELD = "commentCount"
        const val LIKE_COUNT_FIELD = "likeCount"
        const val VIEW_COUNT_FIELD = "viewCount"
    }

}