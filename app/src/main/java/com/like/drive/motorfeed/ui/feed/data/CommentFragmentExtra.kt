package com.like.drive.motorfeed.ui.feed.data

import android.os.Parcelable
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.ReCommentData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CommentFragmentExtra(
    val commentUpdate: Boolean? = false,
    val commentData: CommentData? = null,
    val reCommentData: ReCommentData? = null
) : Parcelable