package com.like.drive.motorfeed.repository.board

import com.like.drive.motorfeed.data.board.CommentData
import com.like.drive.motorfeed.data.board.CommentWrapData
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.board.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import com.like.drive.motorfeed.ui.board.upload.data.BoardUploadField
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BoardRepository {
    suspend fun addFeed(
        boardField: BoardUploadField,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int) -> Unit,
        success: (BoardData) -> Unit,
        fail: () -> Unit
    )

    suspend fun updateFeed(
        boardField: BoardUploadField,
        boardData: BoardData,
        success: (BoardData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeFeed(
        boardData: BoardData,
        success: (BoardData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeUserFeed(
        boardData: BoardData
    )

    suspend fun setLike(bid: String, isUp: Boolean)

    suspend fun getFeedComment(bid: String): Flow<List<CommentData>>

    suspend fun getFeed(
        bid: String,
        success: (BoardData?, List<CommentWrapData>?) -> Unit,
        isLike: (Boolean) -> Unit,
        fail: () -> Unit
    )

    suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData? = null,
        feedTypeData: CategoryData? = null,
        tagStr: String? = null
    ): Flow<List<BoardData>>

    suspend fun getUserFeedList(date: Date, uid: String): Flow<List<BoardData>>

    suspend fun addComment(
        boardData: BoardData,
        comment: String,
        success: (CommentData) -> Unit,
        fail: () -> Unit
    )

    suspend fun addReComment(
        boardData: BoardData,
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

    suspend fun isLikeFeed(bid: String): Boolean
}