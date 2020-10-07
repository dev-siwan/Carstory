package com.like.drive.carstory.ui.motor.binder

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.motor.adapter.SelectMotorTypeAdapter

@BindingAdapter("motorTypeList")
fun RecyclerView.setBindMotorTypeList(data: List<MotorTypeData>?) {
    data?.let {
        (adapter as SelectMotorTypeAdapter).run {
            motorTypeList = it
            notifyDataSetChanged()
            scrollToPosition(0)
        }
    }
}

