package com.like.drive.motorfeed.ui.feed.upload.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.feed.upload.holder.FeedUploadPhotoHolder
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel


class FeedUploadPhotoAdapter(private val viewModelFeed: FeedUploadViewModel) :
    RecyclerView.Adapter<FeedUploadPhotoHolder>() {

    var photoList = arrayListOf<PhotoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedUploadPhotoHolder {
        return FeedUploadPhotoHolder.from(parent)
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holderFeed: FeedUploadPhotoHolder, position: Int) {
        holderFeed.bind(photoList[position], viewModelFeed)
    }

    fun addItem(photoData: PhotoData) {
        photoList.add(photoData)
        notifyItemInserted(photoList.size - 1)
    }

    fun removeItem(data: PhotoData) {
        photoList.firstOrNull { it == data }?.let {
            val index = photoList.indexOf(it)
            photoList.remove(it)
            notifyItemRemoved(index)
        }
    }

}