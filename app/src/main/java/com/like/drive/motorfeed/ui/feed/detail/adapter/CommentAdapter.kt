package com.like.drive.motorfeed.ui.feed.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.data.feed.ReCommentWrapData
import com.like.drive.motorfeed.ui.feed.detail.holder.FeedCommentHolder
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import kotlinx.android.synthetic.main.holder_feed_comment.view.*

class CommentAdapter(val vm: FeedDetailViewModel) : RecyclerView.Adapter<FeedCommentHolder>() {

    var commentList = mutableListOf<CommentWrapData>()
    private var viewHolders: FeedCommentHolder?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedCommentHolder.from(parent).apply {
            lifeCycleCreated()
            viewHolders=this
        }

    override fun getItemCount() = commentList.size

    override fun onBindViewHolder(holder: FeedCommentHolder, position: Int) {
        holder.bind(vm, commentList[position])
    }

    override fun onViewAttachedToWindow(holder: FeedCommentHolder) {
        holder.lifeCycleAttach()
    }

    override fun onViewDetachedFromWindow(holder: FeedCommentHolder) {
        holder.lifeCycleDetach()
    }

    fun lifeCycleDestroyed(){
        viewHolders?.lifeCycleDestroyed()
    }

    fun addReCommentItem(reCommentData: ReCommentData) {
        val originItem = commentList.firstOrNull { it.commentData.cid == reCommentData.cid }
        originItem?.let {
            it.reCommentList.add(reCommentData)
            val updateIndex = commentList.indexOf(it)
            if (updateIndex >= 0) {
                notifyItemChanged(updateIndex)
            }
        }
    }

    fun addComment(commentData: CommentData,closure:()->Unit) {
        commentList.add(CommentWrapData(commentData))
        notifyItemInserted(commentList.size - 1)
        closure()
    }
}

