package com.like.drive.motorfeed.ui.base.binder

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("android:visibility")
fun View.setVisibility(isVisible: Boolean) {
    this.isVisible = isVisible
}