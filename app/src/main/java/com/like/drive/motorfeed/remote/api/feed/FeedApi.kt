package com.like.drive.motorfeed.remote.api.feed

import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.notification.NotificationSendData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.feed.data.FeedCountEnum
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FeedApi {
    suspend fun setFeed(feedData: FeedData): Flow<Boolean>
    suspend fun setUserFeed(uid: String, feedData: FeedData): Flow<Boolean>
    suspend fun removeFeed(feedData: FeedData): Flow<Boolean>
    suspend fun removeUserFeed(feedData: FeedData): Flow<Boolean>
    suspend fun getComment(fid: String): Flow<List<CommentData>>
    suspend fun getReComment(fid: String): Flow<List<ReCommentData>>
    suspend fun getFeed(fid: String): Flow<FeedData?>
    suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData? = null,
        feedTypeData: FeedTypeData? = null,
        tagStr: String? = null
    ): Flow<List<FeedData>>

    suspend fun addComment(commentData: CommentData): Flow<Boolean>
    suspend fun updateCount(fid: String, flag: FeedCountEnum)
    suspend fun addReComment(reCommentData: ReCommentData): Flow<Boolean>
    suspend fun updateComment(commentData: CommentData): Flow<Boolean>
    suspend fun updateReComment(reCommentData: ReCommentData): Flow<Boolean>
    suspend fun removeComment(commentData: CommentData): Flow<Boolean>
    suspend fun removeReComment(reCommentData: ReCommentData): Flow<Boolean>
}