package com.like.drive.motorfeed.repository.feed

import com.google.firebase.firestore.FirebaseFirestore
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.remote.api.feed.FeedApi
import com.like.drive.motorfeed.remote.api.img.ImageApi
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

class FeedRepositoryImpl(private val feedApi: FeedApi,private val imgApi:ImageApi,private val firestore: FirebaseFirestore) : FeedRepository {
    override suspend fun addFeed(feedData: FeedData,photoFileList:List<File>) {
        val documentID = firestore.collection(CollectionName.FEED).document().id
        val imgListUrl = withContext(Dispatchers.IO){imgApi.uploadImageList(documentID,photoFileList)}

        imgListUrl.filter { it ==null }

    }
}