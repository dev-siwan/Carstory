package com.like.drive.carstory.ui.board.detail.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.carstory.ui.board.data.CommentFragmentExtra

@BindingAdapter("commentUser")
fun TextView.setCommentUser(commentFragmentExtra: CommentFragmentExtra?) {

    text = commentFragmentExtra?.let {
        when {
            it.commentData != null -> it.commentData.userInfo?.nickName
            it.reCommentReply != null -> it.reCommentReply.userInfo?.nickName
            else -> null
        }
    }

}

@BindingAdapter("commentContent")
fun TextView.setCommentContent(commentFragmentExtra: CommentFragmentExtra?) {

    text = commentFragmentExtra?.let {
        when {
            it.commentData != null -> it.commentData.commentStr
            it.reCommentReply != null -> it.reCommentReply.commentStr
            else -> null
        }
    }

}