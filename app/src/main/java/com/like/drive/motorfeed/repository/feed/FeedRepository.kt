package com.like.drive.motorfeed.repository.feed

import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.upload.data.FeedUploadField
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FeedRepository {
    suspend fun addFeed(
        feedField: FeedUploadField,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int) -> Unit,
        success: (FeedData) -> Unit,
        fail: () -> Unit
    )
}