package com.like.drive.motorfeed.ui.motor.binder

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.motor.adapter.SelectMotorTypeAdapter
import com.like.drive.motorfeed.ui.motor.viewmodel.MotorTypeViewModel

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

