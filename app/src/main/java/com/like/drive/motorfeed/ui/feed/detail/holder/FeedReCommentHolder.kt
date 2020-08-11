package com.like.drive.motorfeed.ui.feed.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.databinding.HolderFeedCommentBinding
import com.like.drive.motorfeed.databinding.HolderFeedDetailPhotoHolderBinding
import com.like.drive.motorfeed.databinding.HolderFeedReCommentBinding
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel

class FeedReCommentHolder(val binding: HolderFeedReCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm :FeedDetailViewModel,data: ReCommentData) {
        binding.vm = vm
        binding.reCommentData = data
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FeedReCommentHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedReCommentBinding.inflate(layoutInflater, parent, false)

            return FeedReCommentHolder(binding)
        }
    }
}