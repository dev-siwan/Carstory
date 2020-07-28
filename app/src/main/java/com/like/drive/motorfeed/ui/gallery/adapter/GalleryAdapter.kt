package com.like.drive.motorfeed.ui.gallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.HolderGalleryBinding
import com.like.drive.motorfeed.ui.gallery.data.GalleryItemData
import com.like.drive.motorfeed.ui.gallery.holder.GalleryHolder
import com.like.drive.motorfeed.ui.gallery.viewmodel.GalleryViewModel

class GalleryAdapter(val viewModel: GalleryViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var galleryListData: List<GalleryItemData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HolderGalleryBinding>(
            inflater,
            R.layout.holder_gallery,
            parent,
            false
        )
        return GalleryHolder(binding, viewModel)
    }

    override fun getItemCount(): Int = galleryListData.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as GalleryHolder).bind(galleryListData[position])
    }
}