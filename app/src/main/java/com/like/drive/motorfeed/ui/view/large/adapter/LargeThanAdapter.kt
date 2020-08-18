package com.like.drive.motorfeed.ui.view.large.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.view.large.holder.LargeThanViewHolder

class LargeThanAdapter : ListAdapter<PhotoData, LargeThanViewHolder>(object :
    DiffUtil.ItemCallback<PhotoData>() {
    override fun areItemsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PhotoData, newItem: PhotoData): Boolean {
        return oldItem == newItem
    }


}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LargeThanViewHolder.from(parent)

    override fun onBindViewHolder(holder: LargeThanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}