package com.like.drive.carstory.ui.notification.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.ui.notification.holder.NotificationViewHolder
import com.like.drive.carstory.ui.notification.viewmodel.NotificationViewModel

class NotificationAdapter(val vm:NotificationViewModel) : RecyclerView.Adapter<NotificationViewHolder>() {

    val list = mutableListOf<NotificationSendData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotificationViewHolder.from(parent)

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(vm,list[position])
    }

}