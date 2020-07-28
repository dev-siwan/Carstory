package com.like.drive.motorfeed.ui.upload.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.upload.data.PhotoData
import com.like.drive.motorfeed.ui.upload.holder.UploadPhotoHolder
import com.like.drive.motorfeed.ui.upload.viewmodel.UploadViewModel

class UploadPhotoAdapter(val viewModel: UploadViewModel) :
    RecyclerView.Adapter<UploadPhotoHolder>() {

    var photoList: List<PhotoData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadPhotoHolder {
        return UploadPhotoHolder.from(parent)
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: UploadPhotoHolder, position: Int) {
        holder.bind(photoList[position])

    }


}