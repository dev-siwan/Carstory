package com.like.drive.motorfeed.ui.upload.adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.upload.data.PhotoData
import com.like.drive.motorfeed.ui.upload.holder.UploadPhotoHolder
import com.like.drive.motorfeed.ui.upload.viewmodel.UploadViewModel


class UploadPhotoAdapter(val viewModel: UploadViewModel) :
    RecyclerView.Adapter<UploadPhotoHolder>() {

    var photoList = mutableListOf<PhotoData>()

    private var uploadPhotoHolder : UploadPhotoHolder?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadPhotoHolder {
        return UploadPhotoHolder.from(parent).apply {
            lifeCycleCreated()
            uploadPhotoHolder= this
        }
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: UploadPhotoHolder, position: Int) {
        holder.bind(photoList[position],viewModel)
    }


    override fun onViewAttachedToWindow(holder: UploadPhotoHolder) {
        holder.lifeCycleAttach()
    }

    override fun onViewDetachedFromWindow(holder: UploadPhotoHolder) {
        holder.lifeCycleDetach()
    }

    fun addItem(photoData:PhotoData){
        photoList.add(photoData)
        notifyItemInserted(photoList.size-1)
    }

    fun removeItem(data: PhotoData) {
        photoList.firstOrNull { it == data }?.let {
        val index = photoList.indexOf(it)
        photoList.remove(it)
        notifyItemRemoved(index)
        }
    }

    fun lifeCycleDestroyed(){
        uploadPhotoHolder?.lifeCycleDestroyed()
    }

}