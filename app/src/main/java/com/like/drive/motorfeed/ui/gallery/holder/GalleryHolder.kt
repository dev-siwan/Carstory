package com.like.drive.motorfeed.ui.gallery.holder

import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderGalleryBinding
import com.like.drive.motorfeed.ui.gallery.data.GalleryItemData
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel

class GalleryHolder(
    val binding: HolderGalleryBinding,
    viewModel: GalleryViewModel
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.vm = viewModel
    }

    fun bind(data: GalleryItemData) {
        binding.data = data
        binding.executePendingBindings()
    }
}