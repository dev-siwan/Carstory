package com.like.drive.motorfeed.ui.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderHomeFeedListDescBinding
import com.like.drive.motorfeed.databinding.HolderHomeRegisterFeedBinding

class HomeFeedListDescViewHolder(val binding: HolderHomeFeedListDescBinding) :
    RecyclerView.ViewHolder(binding.root) {


    companion object {
        fun from(parent: ViewGroup): HomeFeedListDescViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                HolderHomeFeedListDescBinding.inflate(layoutInflater, parent, false)

            return HomeFeedListDescViewHolder(binding)
        }
    }
}