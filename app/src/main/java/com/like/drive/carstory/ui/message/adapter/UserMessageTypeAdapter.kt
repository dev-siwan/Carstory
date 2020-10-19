package com.like.drive.carstory.ui.message.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.carstory.ui.message.data.UserMessageType
import com.like.drive.carstory.ui.message.holder.UserMessageTypeHolder
import com.like.drive.carstory.ui.message.viewmodel.UserMessageViewModel
import com.like.drive.carstory.ui.report.reg.data.ReportType
import com.like.drive.carstory.ui.report.reg.holder.ReportRegisterViewHolder
import com.like.drive.carstory.ui.report.reg.viewmodel.ReportRegisterViewModel

class UserMessageTypeAdapter(val vm: UserMessageViewModel) :
    ListAdapter<UserMessageType, UserMessageTypeHolder>(
        object : DiffUtil.ItemCallback<UserMessageType>() {
            override fun areItemsTheSame(oldItem: UserMessageType, newItem: UserMessageType) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: UserMessageType, newItem: UserMessageType) =
                oldItem.title == newItem.title

        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserMessageTypeHolder.from(parent)

    override fun onBindViewHolder(holder: UserMessageTypeHolder, position: Int) {
        holder.bind(vm, getItem(position))
    }

}