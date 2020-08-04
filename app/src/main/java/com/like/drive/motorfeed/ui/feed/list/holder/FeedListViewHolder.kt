package com.like.drive.motorfeed.ui.feed.list.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.HolderFeedListBinding

class FeedListViewHolder(val binding: HolderFeedListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(feedData: FeedData) {
        binding.data = feedData
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FeedListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedListBinding.inflate(layoutInflater, parent, false)

            return FeedListViewHolder(binding)
        }
    }
}
