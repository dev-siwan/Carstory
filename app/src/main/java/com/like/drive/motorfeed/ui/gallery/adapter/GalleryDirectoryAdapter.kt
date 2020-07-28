package com.like.drive.motorfeed.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.HolderGalleryDirectoryBinding
import com.like.drive.motorfeed.ui.gallery.data.GalleryDirectoryData
import com.like.drive.motorfeed.ui.gallery.holder.GalleryDirectoryHolder
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel

class GalleryDirectoryAdapter(val viewModel: GalleryViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var directoryData: List<GalleryDirectoryData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HolderGalleryDirectoryBinding>(
            inflater,
            R.layout.holder_gallery_directory,
            parent,
            false
        )
        return GalleryDirectoryHolder(binding, viewModel)
    }

    override fun getItemCount(): Int = directoryData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GalleryDirectoryHolder).bind(directoryData[position])
    }
}