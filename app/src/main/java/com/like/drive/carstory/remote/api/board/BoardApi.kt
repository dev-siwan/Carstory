package com.like.drive.carstory.remote.api.board

import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.board.CommentData
import com.like.drive.carstory.data.board.ReCommentData
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.board.category.data.CategoryData
import com.like.drive.carstory.ui.board.data.LikeCountEnum
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BoardApi {
    suspend fun setBoard(boardData: BoardData): Flow<Boolean>
    suspend fun setUserBoard(uid: String, boardData: BoardData): Flow<Boolean>
    suspend fun removeBoard(boardData: BoardData): Flow<Boolean>
    suspend fun removeUserBoard(boardData: BoardData): Flow<Boolean>
    suspend fun getComment(bid: String): Flow<List<CommentData>>
    suspend fun getReComment(bid: String): Flow<List<ReCommentData>>
    suspend fun getBoard(bid: String): Flow<BoardData?>
    suspend fun getBoardList(
        date: Date,
        motorTypeData: MotorTypeData? = null,
        categoryData: CategoryData? = null,
        tagStr: String? = null
    ): Flow<List<BoardData>>

    suspend fun getUserBoardList(date: Date, uid: String): Flow<List<BoardData>>

    suspend fun addComment(commentData: CommentData): Flow<Boolean>
    suspend fun updateLike(bid: String, uid: String, flag: LikeCountEnum)
    suspend fun addReComment(reCommentData: ReCommentData): Flow<Boolean>
    suspend fun updateComment(commentData: CommentData): Flow<Boolean>
    suspend fun updateReComment(reCommentData: ReCommentData): Flow<Boolean>
    suspend fun removeComment(commentData: CommentData): Flow<Boolean>
    suspend fun removeReComment(reCommentData: ReCommentData): Flow<Boolean>
}