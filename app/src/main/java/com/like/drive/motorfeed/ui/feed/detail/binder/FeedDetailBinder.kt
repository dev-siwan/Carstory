package com.like.drive.motorfeed.ui.feed.detail.binder

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.ui.base.ext.convertDateToString
import com.like.drive.motorfeed.ui.feed.detail.adapter.DetailImgAdapter
import java.util.*
import kotlin.collections.ArrayList

@BindingAdapter(value = ["detailBrandName","detailModelCode","detailModelName"], requireAll = true)
fun TextView.setDetailMotorType(brandName: String?,modelCode:Int, modelName: String?) {
    brandName?.let {
        text = if (modelCode == 0) {
            String.format(context.getString(R.string.motorType_brand_all_format_text), brandName)
        } else {
            String.format(context.getString(R.string.motorType_format_text), brandName, modelName)
        }
    }

}

@BindingAdapter("detailPhotoList")
fun RecyclerView.setDetailPhotoList(list:List<String>?){
    list?.let {
        (adapter as DetailImgAdapter).submitList(list)
    }
}



@BindingAdapter("formatDate")
fun TextView.setFormatDate(date: Date?){
    date?.let {
       text=it.convertDateToString()
    }
}