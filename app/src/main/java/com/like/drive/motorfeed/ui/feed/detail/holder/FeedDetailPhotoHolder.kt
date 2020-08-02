package com.like.drive.motorfeed.ui.feed.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderFeedDetailPhotoHolderBinding
import com.like.drive.motorfeed.databinding.HolderSelectMotorTypeItemBinding
import com.like.drive.motorfeed.ui.motor.holder.SelectMotorTypeHolder

class FeedDetailPhotoHolder (val binding:HolderFeedDetailPhotoHolderBinding):
    RecyclerView.ViewHolder(binding.root){

    fun bind(imgUrl:String?){
        binding.imgUrl = imgUrl
    }

    companion object {
        fun from(parent: ViewGroup): FeedDetailPhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedDetailPhotoHolderBinding.inflate(layoutInflater, parent, false)

            return FeedDetailPhotoHolder(binding)
        }
    }
}