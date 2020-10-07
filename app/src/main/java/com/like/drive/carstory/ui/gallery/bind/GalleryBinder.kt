package com.like.drive.carstory.ui.gallery.bind

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.ui.gallery.adapter.GalleryAdapter
import com.like.drive.carstory.ui.gallery.adapter.GalleryDirectoryAdapter
import com.like.drive.carstory.ui.gallery.data.GalleryDirectoryData
import com.like.drive.carstory.ui.gallery.data.GalleryItemData

@BindingAdapter("galleryItem")
fun RecyclerView.setGalleryItem(data: List<GalleryItemData>?) {
    data?.let {
        (adapter as? GalleryAdapter)?.run {
            init(data)
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