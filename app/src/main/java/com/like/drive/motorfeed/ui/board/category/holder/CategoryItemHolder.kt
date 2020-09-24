package com.like.drive.motorfeed.ui.board.category.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderCategoryItemBinding
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import com.like.drive.motorfeed.ui.board.upload.viewmodel.UploadViewModel

class CategoryItemHolder (val binding: HolderCategoryItemBinding):
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm:UploadViewModel, data: CategoryData) {
        binding.item = data
        binding.vm = vm
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CategoryItemHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderCategoryItemBinding.inflate(layoutInflater, parent, false)

            return CategoryItemHolder(binding)
        }
    }
}