package com.like.drive.carstory.ui.search.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.carstory.ui.search.data.RecentlyData
import com.like.drive.carstory.ui.search.holder.RecentlyViewHolder
import com.like.drive.carstory.ui.search.viewmodel.SearchViewModel

class RecentlyListAdapter(val vm: SearchViewModel) :
    ListAdapter<RecentlyData, RecentlyViewHolder>(object : DiffUtil.ItemCallback<RecentlyData>() {
        override fun areItemsTheSame(oldItem: RecentlyData, newItem: RecentlyData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RecentlyData, newItem: RecentlyData): Boolean {
            return oldItem.createDate == newItem.createDate
        }

    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentlyViewHolder =
        RecentlyViewHolder.from(parent)

    override fun onBindViewHolder(holder: RecentlyViewHolder, position: Int) {
        holder.bind(vm, getItem(position))
    }


}