package com.like.drive.motorfeed.ui.feed.upload.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderUploadPhotoBinding
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel

class FeedUploadPhotoHolder(val binding:HolderUploadPhotoBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(item: PhotoData, vm: FeedUploadViewModel) {
        binding.item = item
        binding.vm= vm
    }

    companion object {
        fun from(parent: ViewGroup): FeedUploadPhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderUploadPhotoBinding.inflate(layoutInflater, parent, false)

            return FeedUploadPhotoHolder(binding)
        }
    }

}