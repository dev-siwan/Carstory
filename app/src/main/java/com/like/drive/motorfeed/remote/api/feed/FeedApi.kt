package com.like.drive.motorfeed.remote.api.feed

import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import kotlinx.coroutines.flow.Flow

interface FeedApi{
   suspend fun addFeed(feedData: FeedData): Flow<Boolean>
   suspend fun addUserFeed(uid:String,feedData: FeedData):Flow<Boolean>
   suspend fun getComment(fid:String):Flow<List<CommentData>>
   suspend fun getFeed(fid:String):Flow<FeedData?>
   suspend fun getFeedList(brandCode: Int?, modelCode: Int?): Flow<List<FeedData>>
}