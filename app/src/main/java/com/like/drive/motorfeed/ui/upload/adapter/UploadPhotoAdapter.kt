package com.like.drive.motorfeed.ui.upload.adapter

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.upload.data.PhotoData
import com.like.drive.motorfeed.ui.upload.holder.UploadPhotoHolder
import com.like.drive.motorfeed.ui.upload.viewmodel.UploadViewModel

class UploadPhotoAdapter(val activity: Activity,val viewModel: UploadViewModel) :
    RecyclerView.Adapter<UploadPhotoHolder>() {

    var photoList: List<PhotoData> = emptyList()

    var uploadPhotoHolder : UploadPhotoHolder?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UploadPhotoHolder {
        return UploadPhotoHolder.from(parent).apply {
            lifeCycleCreated()
            uploadPhotoHolder= this
        }
    }

    override fun getItemCount(): Int = photoList.size

    override fun onBindViewHolder(holder: UploadPhotoHolder, position: Int) {
        holder.bind(activity,photoList[position],viewModel)
    }


    override fun onViewAttachedToWindow(holder: UploadPhotoHolder) {
        holder.lifeCycleAttach()
    }

    override fun onViewDetachedFromWindow(holder: UploadPhotoHolder) {
        holder.lifeCycleDetach()
    }


    fun lifeCycleDestroyed(){
        uploadPhotoHolder?.let {
            it.lifeCycleDestroyed()
        }
    }




}