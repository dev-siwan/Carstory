package com.like.drive.motorfeed.ui.upload.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.upload.adapter.UploadPhotoAdapter
import com.like.drive.motorfeed.data.photo.PhotoData

@BindingAdapter("uploadPhotoItems")
fun RecyclerView.setRegPhotoItems(data: List<PhotoData>?) {
    data?.let {
        (adapter as? UploadPhotoAdapter)?.run {
            this.photoList = data.toMutableList()
            notifyDataSetChanged()
        }
    }
}
@BindingAdapter("formatMotorTypeData")
fun TextView.setMotorType(data:MotorTypeData?){
    data?.let {
        text = if (it.modelCode == 0) {
            String.format(context.getString(R.string.motorType_brand_all_format_text),it.brandName)
        }else{
            String.format(context.getString(R.string.motorType_format_text),it.brandName,it.brandName)
        }
    }
}