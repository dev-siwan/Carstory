package com.like.drive.carstory.repository.board

import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.board.CommentData
import com.like.drive.carstory.data.board.CommentWrapData
import com.like.drive.carstory.data.board.ReCommentData
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.data.report.ReportData
import com.like.drive.carstory.ui.board.category.data.CategoryData
import com.like.drive.carstory.ui.board.upload.data.BoardUploadField
import kotlinx.coroutines.flow.Flow
import java.util.*

interface BoardRepository {
    suspend fun addBoard(
        boardField: BoardUploadField,
        photoFileList: ArrayList<PhotoData>,
        photoSuccessCount: (Int) -> Unit,
        success: (BoardData) -> Unit,
        fail: () -> Unit
    )

    suspend fun updateBoard(
        boardField: BoardUploadField,
        boardData: BoardData,
        success: (BoardData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeBoard(
        boardData: BoardData,
        success: (BoardData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeEmptyBoard(
        boardData: BoardData
    )

    suspend fun setLike(bid: String, isUp: Boolean)

    suspend fun getBoardComment(bid: String): Flow<List<CommentData>>

    suspend fun getBoard(
        bid: String,
        success: (BoardData?, List<CommentWrapData>?) -> Unit,
        isLike: (Boolean) -> Unit,
        fail: () -> Unit
    )

    suspend fun getBoardList(
        date: Date,
        motorTypeData: MotorTypeData? = null,
        categoryData: CategoryData? = null,
        tagStr: String? = null
    ): Flow<List<BoardData>>

    suspend fun getUserBoardList(date: Date, uid: String): Flow<List<BoardData>>

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

    suspend fun isLikeBoard(bid: String): Boolean

    suspend fun removeAllLike()

    suspend fun sendReport(reportData: ReportData,
                           success: () -> Unit,
                           fail: () -> Unit)
}