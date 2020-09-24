package com.like.drive.motorfeed.remote.api.board

import com.like.drive.motorfeed.data.board.CommentData
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.board.ReCommentData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.board.data.LikeCountEnum
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BoardApi {
    suspend fun setFeed(boardData: BoardData): Flow<Boolean>
    suspend fun setUserFeed(uid: String, boardData: BoardData): Flow<Boolean>
    suspend fun removeFeed(boardData: BoardData): Flow<Boolean>
    suspend fun removeUserFeed(boardData: BoardData): Flow<Boolean>
    suspend fun getComment(fid: String): Flow<List<CommentData>>
    suspend fun getReComment(fid: String): Flow<List<ReCommentData>>
    suspend fun getFeed(fid: String): Flow<BoardData?>
    suspend fun getFeedList(
        date: Date,
        motorTypeData: MotorTypeData? = null,
        feedTypeData: CategoryData? = null,
        tagStr: String? = null
    ): Flow<List<BoardData>>


    suspend fun getUserFeedList(date:Date,uid: String):Flow<List<BoardData>>

    suspend fun addComment(commentData: CommentData): Flow<Boolean>
    suspend fun updateCount(fid: String, flag: LikeCountEnum)
    suspend fun addReComment(reCommentData: ReCommentData): Flow<Boolean>
    suspend fun updateComment(commentData: CommentData): Flow<Boolean>
    suspend fun updateReComment(reCommentData: ReCommentData): Flow<Boolean>
    suspend fun removeComment(commentData: CommentData): Flow<Boolean>
    suspend fun removeReComment(reCommentData: ReCommentData): Flow<Boolean>
}