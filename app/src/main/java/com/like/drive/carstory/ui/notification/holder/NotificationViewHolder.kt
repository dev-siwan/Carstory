package com.like.drive.carstory.ui.notification.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.notification.NotificationSendData
import com.like.drive.carstory.databinding.HolderNotificationItemBinding
import com.like.drive.carstory.ui.notification.viewmodel.NotificationViewModel

class NotificationViewHolder(val binding: HolderNotificationItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: NotificationViewModel, data: NotificationSendData) {
        binding.data = data
        binding.vm = vm
    }

    companion object {
        fun from(parent: ViewGroup): NotificationViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderNotificationItemBinding.inflate(layoutInflater, parent, false)

            return NotificationViewHolder(binding)
        }
    }

}