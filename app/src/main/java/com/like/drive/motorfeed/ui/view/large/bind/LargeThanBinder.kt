package com.like.drive.motorfeed.ui.view.large.bind

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.view.large.adapter.LargeThanAdapter

@BindingAdapter("largePhotoDataList")
fun RecyclerView.setLargePhotoDataList(list: List<PhotoData>?) {
    list?.let {
        run {
            (adapter as LargeThanAdapter).submitList(it)
        }
    }
}