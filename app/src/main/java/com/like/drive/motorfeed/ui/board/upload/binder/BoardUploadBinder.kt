package com.like.drive.motorfeed.ui.board.upload.binder

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.board.upload.adapter.BoardUploadPhotoAdapter
import com.like.drive.motorfeed.data.photo.PhotoData

@BindingAdapter("uploadPhotoItems")
fun RecyclerView.setRegPhotoItems(data: List<PhotoData>?) {
    data?.let {
        (adapter as? BoardUploadPhotoAdapter)?.run {
            this.photoList = data as ArrayList<PhotoData>
            notifyDataSetChanged()
        }
    }
}

@BindingAdapter("formatMotorTypeData")
fun TextView.setMotorType(data:MotorTypeData?){
    text = data?.let {
        if (it.modelCode == 0) {
            it.brandName
        }else{
            String.format(context.getString(R.string.motorType_format_text),it.brandName,it.modelName)
        }
    }
}

@BindingAdapter(value=["isImgUpload","uploadPhotoCount","uploadMaxCount"] ,requireAll = true)
fun TextView.setUploadProgress(isImgUpload:Boolean?,uploadPhotoCount:Int?,uploadMaxCount:Int?){
    isImgUpload?.let {
        text = if (it) {
            String.format(
                context.getString(R.string.upload_progress_img_format),
                uploadPhotoCount,
                uploadMaxCount
            )
        } else {
            context.getString(R.string.upload_progress_feed_update)
        }
    }
}

@BindingAdapter("tagList")
fun Button.setTagList(tagList:ArrayList<String>?){
    tagList?.let {
    text = it.joinToString(", #","#")
    }
}

@BindingAdapter("tagList")
fun TextView.setTagList(tagList:List<String>?){
    tagList?.let {
        text = it.joinToString(", #","#")
    }
}