package com.like.drive.carstory.ui.message.data

import android.content.Context
import androidx.databinding.ObservableBoolean
import com.like.drive.carstory.R

data class UserMessageType(
    val title: String? = null
) {
    fun getList(context: Context) = context.resources.getStringArray(R.array.pick_report_type)
        .map { UserMessageType(it) }
}

