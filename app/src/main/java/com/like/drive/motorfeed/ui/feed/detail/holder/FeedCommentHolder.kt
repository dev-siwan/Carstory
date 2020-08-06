package com.like.drive.motorfeed.ui.feed.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.databinding.HolderFeedCommentBinding
import com.like.drive.motorfeed.databinding.HolderFeedDetailPhotoHolderBinding

class FeedCommentHolder (val binding:HolderFeedCommentBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(data:CommentData){
        binding.commentData = data
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FeedCommentHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedCommentBinding.inflate(layoutInflater, parent, false)

            return FeedCommentHolder(binding)
        }
    }
}