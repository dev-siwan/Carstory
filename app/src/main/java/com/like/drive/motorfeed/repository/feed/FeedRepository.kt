package com.like.drive.motorfeed.repository.feed

import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    suspend fun addFeed(
        feedField: FeedUploadField,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int) -> Unit,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    )

    suspend fun setLike(fid: String, isUp: Boolean)
    suspend fun getFeedComment(fid: String): Flow<List<CommentData>>
    suspend fun getFeed(
        fid: String,
        success: (FeedData?, List<CommentData>?) -> Unit,
        fail: () -> Unit
    )

    suspend fun getFeedList(brandCode: Int? = null, modelCode: Int? = null): Flow<List<FeedData>>

    suspend fun addComment(
        fid: String,
        comment: String,
        success: (CommentData) -> Unit,
        fail: () -> Unit
    )

    suspend fun addReComment(
        fid: String,
        cid: String,
        comment: String,
        success: (ReCommentData) -> Unit,
        fail: () -> Unit
    )
}