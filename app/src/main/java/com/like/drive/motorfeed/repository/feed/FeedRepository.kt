package com.like.drive.motorfeed.repository.feed

import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FeedRepository {
    suspend fun addFeed(
        feedField: FeedUploadField,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int) -> Unit,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    )

    suspend fun updateFeed(
        feedField: FeedUploadField,
        feedData: FeedData,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeFeed(
        feedData: FeedData,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeUserFeed(
        feedData: FeedData
    )

    suspend fun setLike(fid: String, isUp: Boolean)

    suspend fun getFeedComment(fid: String): Flow<List<CommentData>>

    suspend fun getFeed(
        fid: String,
        success: (FeedData?, List<CommentWrapData>?) -> Unit,
        isLike: (Boolean) -> Unit,
        fail: () -> Unit
    )

    suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData? = null,
        feedTypeData: FeedTypeData? = null,
        tagStr: String? = null
    ): Flow<List<FeedData>>

    suspend fun getUserFeedList(date: Date, uid: String): Flow<List<FeedData>>

    suspend fun addComment(
        feedData: FeedData,
        comment: String,
        success: (CommentData) -> Unit,
        fail: () -> Unit
    )

    suspend fun addReComment(
        feedData: FeedData,
        commentData: CommentData,
        reComment: String,
        success: (ReCommentData) -> Unit,
        fail: () -> Unit
    )

    suspend fun updateComment(
        comment: CommentData,
        success: () -> Unit,
        fail: () -> Unit
    )

    suspend fun updateReComment(
        reCommentData: ReCommentData,
        success: () -> Unit,
        fail: () -> Unit
    )

    suspend fun removeComment(
        comment: CommentData,
        success: () -> Unit,
        fail: () -> Unit
    )

    suspend fun removeReComment(
        reCommentData: ReCommentData,
        success: () -> Unit,
        fail: () -> Unit
    )

    suspend fun isLikeFeed(fid: String): Boolean
}