package com.like.drive.carstory.ui.message.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.databinding.HolderReportRegisterItemBinding
import com.like.drive.carstory.databinding.HolderUserMessageItemBinding
import com.like.drive.carstory.ui.message.data.UserMessageType
import com.like.drive.carstory.ui.message.viewmodel.UserMessageViewModel
import com.like.drive.carstory.ui.report.reg.data.ReportType
import com.like.drive.carstory.ui.report.reg.viewmodel.ReportRegisterViewModel

class UserMessageTypeHolder(val binding: HolderUserMessageItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: UserMessageViewModel, userMessageType: UserMessageType) {
        binding.data = userMessageType
        binding.vm = vm
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): UserMessageTypeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderUserMessageItemBinding.inflate(layoutInflater, parent, false)

            return UserMessageTypeHolder(binding)
        }
    }
}