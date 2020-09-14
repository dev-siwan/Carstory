package com.like.drive.motorfeed.ui.notification.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.notification.NotificationType

@BindingAdapter("notificationType")
fun TextView.setNotificationTitle(type: String?) {
    type?.let {
        text = NotificationType.values().find { type -> type.value == it }?.title
            ?: context.getString(R.string.app_name_kr)
    }
}