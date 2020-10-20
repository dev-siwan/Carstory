package com.like.drive.carstory.ui.base.binder

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

@BindingAdapter("refreshing")
fun SwipeRefreshLayout.refreshing(visible: Boolean) {
    isRefreshing = visible
}

@BindingAdapter("enable")
fun SwipeRefreshLayout.enable(status:Boolean){
    isEnabled = status
}