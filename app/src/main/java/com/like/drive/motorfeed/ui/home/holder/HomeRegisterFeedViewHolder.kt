package com.like.drive.motorfeed.ui.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderFeedListBinding
import com.like.drive.motorfeed.databinding.HolderHomeRegisterFeedBinding
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListViewHolder
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel

class HomeRegisterFeedViewHolder(val binding: HolderHomeRegisterFeedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: HomeViewModel) {

    }

    companion object {
        fun from(parent: ViewGroup): HomeRegisterFeedViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderHomeRegisterFeedBinding.inflate(layoutInflater, parent, false)

            return HomeRegisterFeedViewHolder(binding)
        }
    }

}