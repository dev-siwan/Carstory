package com.like.drive.motorfeed.common.binder

import android.text.InputType
import android.widget.EditText
import androidx.databinding.BindingAdapter

@BindingAdapter("inputTypeFlag")
fun EditText.setInputTypeBinder(inputType: Int){
    this.inputType = inputType
}