package com.like.drive.motorfeed.ui.board.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.motorfeed.ui.board.detail.holder.DetailPhotoHolder
import com.like.drive.motorfeed.ui.board.detail.viewmodel.BoardDetailViewModel

class DetailImgAdapter(val vm :BoardDetailViewModel): ListAdapter<String, DetailPhotoHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DetailPhotoHolder.from(parent)

    override fun onBindViewHolder(holder: DetailPhotoHolder, position: Int) {
        holder.bind(vm,getItem(position))
    }
}

