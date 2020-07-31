package com.like.drive.motorfeed.repository.feed

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.feed.FeedData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FeedRepository {
    suspend fun addFeed(feedData: FeedData,photoFileList:List<File>)
}