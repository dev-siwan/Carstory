package com.like.drive.motorfeed.ui.notice.list.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.databinding.HolderNoticeListItemBinding
import com.like.drive.motorfeed.ui.notice.list.viewmodel.NoticeListViewModel

class NoticeListViewHolder(val binding: HolderNoticeListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: NoticeListViewModel, noticeData: NoticeData) {
        binding.vm = vm
        binding.data = noticeData
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): NoticeListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderNoticeListItemBinding.inflate(layoutInflater, parent, false)

            return NoticeListViewHolder(binding)
        }
    }

}