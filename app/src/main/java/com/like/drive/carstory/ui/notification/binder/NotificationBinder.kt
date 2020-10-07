package com.like.drive.carstory.ui.notification.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.carstory.R
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.data.notification.NotificationType
import com.like.drive.carstory.ui.base.binder.setFormatHtml

@BindingAdapter("notificationType")
fun TextView.setNotificationTitle(data: NotificationSendData?) {
    data?.let {
        when (NotificationType.values().find { type -> type.value == it.notificationType }) {
            NotificationType.NOTICE -> {
                text = data.title
            }
            NotificationType.COMMENT -> {
                setFormatHtml(context.getString(R.string.notification_comment_title, data.title))
            }
            else -> {
                setFormatHtml(context.getString(R.string.notification_re_comment_title, data.title))
            }
        }
    }
}