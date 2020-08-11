package com.like.drive.motorfeed.ui.feed.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.motorfeed.ui.feed.detail.holder.FeedDetailPhotoHolder

class DetailImgAdapter: ListAdapter<String, FeedDetailPhotoHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedDetailPhotoHolder.from(parent)

    override fun onBindViewHolder(holder: FeedDetailPhotoHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

