package com.like.drive.motorfeed.ui.board.list.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.databinding.HolderListBinding
import com.like.drive.motorfeed.ui.board.list.viewmodel.ListViewModel

class ListViewHolder(val binding: HolderListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: ListViewModel, boardData: BoardData) {
        binding.vm = vm
        binding.data = boardData
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderListBinding.inflate(layoutInflater, parent, false)

            return ListViewHolder(binding)
        }
    }
}
