package com.like.drive.carstory.ui.board.upload.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.databinding.HolderUploadPhotoBinding
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.ui.board.upload.viewmodel.UploadViewModel

class BoardUploadPhotoHolder(val binding:HolderUploadPhotoBinding):RecyclerView.ViewHolder(binding.root){

    fun bind(item: PhotoData, vm: UploadViewModel) {
        binding.item = item
        binding.vm= vm
    }

    companion object {
        fun from(parent: ViewGroup): BoardUploadPhotoHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderUploadPhotoBinding.inflate(layoutInflater, parent, false)

            return BoardUploadPhotoHolder(binding)
        }
    }

}