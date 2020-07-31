package com.like.drive.motorfeed.ui.upload.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.upload.holder.UploadPhotoHolder
import com.like.drive.motorfeed.ui.upload.viewmodel.UploadViewModel


class UploadPhotoAdapter(val viewModel: UploadViewModel) :
    RecyclerView.Adapter<UploadPhotoHolder>() {

    var photoList = mutableListOf<PhotoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadPhotoHolder {
        return UploadPhotoHolder.from(parent)
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: UploadPhotoHolder, position: Int) {
        holder.bind(photoList[position], viewModel)
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