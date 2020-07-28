package com.like.drive.motorfeed.ui.gallery.holder

import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderGalleryDirectoryBinding
import com.like.drive.motorfeed.ui.gallery.data.GalleryDirectoryData
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel

class GalleryDirectoryHolder(
    val binding: HolderGalleryDirectoryBinding,
    viewModel: GalleryViewModel
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.vm = viewModel
    }

    fun bind(data: GalleryDirectoryData) {
        binding.data = data
        binding.executePendingBindings()
    }
}