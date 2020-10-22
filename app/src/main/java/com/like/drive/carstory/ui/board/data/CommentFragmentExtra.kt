package com.like.drive.carstory.ui.board.data

import android.os.Parcelable
import com.like.drive.carstory.data.board.CommentData
import com.like.drive.carstory.data.board.ReCommentData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentFragmentExtra(
    val commentUpdate: Boolean? = false,
    val commentData: CommentData? = null,
    val reCommentData: ReCommentData? = null,
    val reCommentReply:ReCommentData?=null,
) : Parcelable