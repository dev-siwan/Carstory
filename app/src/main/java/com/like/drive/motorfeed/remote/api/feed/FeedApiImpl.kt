package com.like.drive.motorfeed.remote.api.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow

class FeedApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : FeedApi {
    override suspend fun addFeed(feedData: FeedData): Flow<Boolean> {
        val documentID = fireStore.collection(CollectionName.FEED).document().id
        feedData.fid = documentID
        return fireBaseTask.setData(fireStore.collection(CollectionName.FEED).document(documentID),feedData)
    }
}