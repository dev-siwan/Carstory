package com.like.drive.motorfeed.ui.home.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderUserFilterHeaderBinding
import com.like.drive.motorfeed.ui.home.viewmodel.UserFilterViewModel

class UserFilterHeaderViewHolder(val binding: HolderUserFilterHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: UserFilterViewModel) {
        binding.vm = vm
    }

    companion object {
        fun from(parent: ViewGroup): UserFilterHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderUserFilterHeaderBinding.inflate(layoutInflater, parent, false)

            return UserFilterHeaderViewHolder(binding)
        }
    }

}