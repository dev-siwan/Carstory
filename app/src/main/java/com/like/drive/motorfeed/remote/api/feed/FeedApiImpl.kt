package com.like.drive.motorfeed.remote.api.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import com.like.drive.motorfeed.remote.reference.CollectionName.FEED_COMMENT
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.flow.Flow

class FeedApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : FeedApi {
    override suspend fun addFeed(feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.setData(fireStore.collection(CollectionName.FEED).document(feedData.fid?:""),feedData)
    }

    override suspend fun addUserFeed(uid: String, feedData: FeedData): Flow<Boolean> {
        return fireBaseTask.setData(fireStore.collection(CollectionName.USER).document(uid).collection(CollectionName.FEED).document(feedData.fid?:""),feedData)
    }

    override suspend fun getComment(fid: String): Flow<List<CommentData>> {
        val collection = fireStore.collection(CollectionName.FEED).document(fid).collection(FEED_COMMENT)
        return fireBaseTask.getData(collection,CommentData::class.java)
    }

    override suspend fun getFeed(fid: String): Flow<FeedData?> {
        val document= fireStore.collection(CollectionName.FEED).document(fid)
        return  fireBaseTask.getData(document,FeedData::class.java)
    }

    override suspend fun getFeedList(brandCode: Int?, modelCode: Int?): Flow<List<FeedData>> {
        val feedCollection = fireStore.collection(CollectionName.FEED)
        return fireBaseTask.getData(feedCollection,FeedData::class.java)
    }

    override suspend fun addComment(commentData: CommentData): Flow<Boolean> {
        val commentFeedCollection = fireStore.collection(CollectionName.FEED).document(commentData.fid?:"").collection(FEED_COMMENT)
        val cid = commentFeedCollection.document().id
        commentData.cid = cid

        return fireBaseTask.setData(commentFeedCollection,commentData)
    }

}