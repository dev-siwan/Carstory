package com.like.drive.motorfeed.ui.feed.type.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderFeedTypeItemBinding
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeItem
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel

class FeedTypeItemHolder (val binding: HolderFeedTypeItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm:FeedUploadViewModel,item: FeedTypeItem) {
        binding.item = item
        binding.vm = vm
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FeedTypeItemHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedTypeItemBinding.inflate(layoutInflater, parent, false)

            return FeedTypeItemHolder(binding)
        }
    }
}