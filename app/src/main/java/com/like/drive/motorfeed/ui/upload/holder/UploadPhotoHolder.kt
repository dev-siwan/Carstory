package com.like.drive.motorfeed.ui.upload.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderUploadPhotoBinding
import com.like.drive.motorfeed.ui.upload.data.PhotoData

class UploadPhotoHolder(val binding:HolderUploadPhotoBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(item:PhotoData){
        binding.item = item
    }

    companion object {
        fun from(parent: ViewGroup): UploadPhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderUploadPhotoBinding.inflate(layoutInflater, parent, false)

            return UploadPhotoHolder(binding)
        }
    }
}