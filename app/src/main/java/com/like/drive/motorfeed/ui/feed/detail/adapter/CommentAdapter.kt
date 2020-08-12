package com.like.drive.motorfeed.ui.feed.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.ui.feed.detail.holder.FeedCommentHolder
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel

class CommentAdapter(val vm: FeedDetailViewModel) : RecyclerView.Adapter<FeedCommentHolder>() {

    var commentList = mutableListOf<CommentWrapData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedCommentHolder.from(parent)

    override fun getItemCount() = commentList.size

    override fun onBindViewHolder(holder: FeedCommentHolder, position: Int) {
        holder.bind(vm, commentList[position])
    }


    fun addReCommentItem(reCommentData: ReCommentData) {
        val originItem = commentList.find { it.commentData.cid == reCommentData.cid }
        originItem?.let {
            it.reCommentList.add(reCommentData)
            val updateIndex = commentList.indexOf(it)
            if (updateIndex >= 0) {
                notifyItemChanged(updateIndex)
            }
        }
    }

    fun updateReCommentItem(reCommentData: ReCommentData) {
        val originItem = commentList.find { it.commentData.cid == reCommentData.cid }
        originItem?.let { commentWrapData ->
            val updateIndex = commentList.indexOf(commentWrapData)

            val reOriginItem = commentWrapData.reCommentList.find { it.rcId == reCommentData.rcId }
            reOriginItem?.let { reCommentData ->
                val findIndex = commentWrapData.reCommentList.indexOf(reCommentData)
                commentWrapData.reCommentList[findIndex] = reCommentData
            }

            commentList[updateIndex] = originItem
            notifyItemChanged(updateIndex)
        }
    }


    fun removeReCommentItem(reCommentData: ReCommentData) {
        val originItem = commentList.find { it.commentData.cid == reCommentData.cid }

        originItem?.let {
            val updateIndex = commentList.indexOf(it)
            it.reCommentList.remove(reCommentData)
            if (updateIndex >= 0) {
                notifyItemChanged(updateIndex)
            }
        }
    }

    fun addCommentItem(commentData: CommentData) {
        commentList.add(CommentWrapData(commentData))
        notifyItemInserted(commentList.size - 1)
    }

    fun updateCommentItem(commentData: CommentData){
        val originItem = commentList.find { it.commentData.cid == commentData.cid }
        originItem?.let {
            val updateIndex = commentList.indexOf(it)
            it.commentData = commentData
            commentList[updateIndex] = originItem
            notifyItemChanged(updateIndex)
        }
    }

    fun removeCommentItem(commentData: CommentData) {
        val originItem = commentList.find { it.commentData.cid == commentData.cid }
        originItem?.let {
            val removeIndex = commentList.indexOf(it)
            commentList.removeAt(removeIndex)
            notifyItemRemoved(removeIndex)
        }
    }


}

