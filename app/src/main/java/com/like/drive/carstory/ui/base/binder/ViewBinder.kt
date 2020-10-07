package com.like.drive.carstory.ui.base.binder

import android.view.View
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("switchCompatListener")
fun SwitchCompat.setChangeListener(action: ((Boolean) -> Unit)?) {
    setOnCheckedChangeListener { _, isChecked ->
        action?.invoke(isChecked)
    }
}