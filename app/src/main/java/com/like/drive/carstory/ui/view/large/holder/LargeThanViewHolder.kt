package com.like.drive.carstory.ui.view.large.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.databinding.HolderLargeThanBinding

class LargeThanViewHolder(val binding: HolderLargeThanBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photoData: PhotoData) {
        binding.photoData = photoData
    }

    companion object {
        fun from(parent: ViewGroup): LargeThanViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderLargeThanBinding.inflate(layoutInflater, parent, false)

            return LargeThanViewHolder(binding)
        }
    }
}