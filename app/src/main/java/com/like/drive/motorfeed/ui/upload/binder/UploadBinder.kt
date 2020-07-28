package com.like.drive.motorfeed.ui.upload.binder

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.upload.adapter.UploadPhotoAdapter
import com.like.drive.motorfeed.ui.upload.data.PhotoData

@BindingAdapter("uploadPhotoItems")
fun RecyclerView.setRegPhotoItems(data: List<PhotoData>?) {
    data?.let {
        (adapter as? UploadPhotoAdapter)?.run {
            this.photoList = data
            notifyDataSetChanged()
        }
    }
}