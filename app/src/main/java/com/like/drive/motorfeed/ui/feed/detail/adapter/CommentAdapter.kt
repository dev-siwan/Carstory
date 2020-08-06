package com.like.drive.motorfeed.ui.feed.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.ui.feed.detail.holder.FeedCommentHolder
import com.like.drive.motorfeed.ui.feed.detail.holder.FeedDetailPhotoHolder
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel

class CommentAdapter: RecyclerView.Adapter<FeedCommentHolder>() {

    var commentList = mutableListOf<CommentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedCommentHolder.from(parent)

    override fun onBindViewHolder(holder: FeedCommentHolder, position: Int) {
        holder.bind(commentList[position])
    }

    override fun getItemCount()= commentList.size

    fun addComment(commentData: CommentData){
        commentList.add(commentData)
        notifyItemInserted(commentList.size -1 )
    }
}

