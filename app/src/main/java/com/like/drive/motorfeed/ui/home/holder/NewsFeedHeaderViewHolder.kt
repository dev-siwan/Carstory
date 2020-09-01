package com.like.drive.motorfeed.ui.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderNewsFeedHeaderBinding
import com.like.drive.motorfeed.ui.home.viewmodel.NewsFeedViewModel

class NewsFeedHeaderViewHolder(val binding: HolderNewsFeedHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm:NewsFeedViewModel) {

    }

    companion object {
        fun from(parent: ViewGroup): NewsFeedHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderNewsFeedHeaderBinding.inflate(layoutInflater, parent, false)

            return NewsFeedHeaderViewHolder(binding)
        }
    }

}