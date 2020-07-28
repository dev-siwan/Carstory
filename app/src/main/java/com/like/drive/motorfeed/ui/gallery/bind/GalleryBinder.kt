package com.like.drive.motorfeed.ui.gallery.bind

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.gallery.adapter.GalleryAdapter
import com.like.drive.motorfeed.ui.gallery.adapter.GalleryDirectoryAdapter
import com.like.drive.motorfeed.ui.gallery.data.GalleryDirectoryData
import com.like.drive.motorfeed.ui.gallery.data.GalleryItemData

@BindingAdapter("galleryItem")
fun RecyclerView.setGalleryItem(data: List<GalleryItemData>?) {
    data?.let {
        (adapter as? GalleryAdapter)?.run {
            this.galleryListData = data
            notifyDataSetChanged()
        }
    }
}

@BindingAdapter("galleryDirectory")
fun RecyclerView.setGalleryDirectory(data: List<GalleryDirectoryData>?) {
    data?.let {
        (adapter as? GalleryDirectoryAdapter)?.run {
            this.directoryData = data
            notifyDataSetChanged()
        }
    }
}