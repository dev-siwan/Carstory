package com.like.drive.motorfeed.ui.feed.tag.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderFeedTagBinding
import com.like.drive.motorfeed.ui.feed.tag.viewmodel.FeedTagViewModel

class FeedTagViewHolder(val binding:HolderFeedTagBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(viewModel:FeedTagViewModel,tag:String){
        binding.vm = viewModel
        binding.tag = tag
    }

    companion object {
        fun from(parent: ViewGroup): FeedTagViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedTagBinding.inflate(layoutInflater, parent, false)

            return FeedTagViewHolder(binding)
        }
    }
}