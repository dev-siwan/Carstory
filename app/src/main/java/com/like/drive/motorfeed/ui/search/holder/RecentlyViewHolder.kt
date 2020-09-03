package com.like.drive.motorfeed.ui.search.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderRecentlyBinding
import com.like.drive.motorfeed.databinding.HolderSelectMotorTypeItemBinding
import com.like.drive.motorfeed.ui.motor.holder.SelectMotorTypeHolder
import com.like.drive.motorfeed.ui.search.data.RecentlyData
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel

class RecentlyViewHolder(val binding: HolderRecentlyBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: SearchViewModel, data: RecentlyData) {
        binding.data = data
        binding.vm= vm
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RecentlyViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderRecentlyBinding.inflate(layoutInflater, parent, false)

            return RecentlyViewHolder(binding)
        }
    }
}