package com.like.drive.carstory.ui.board.list.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.databinding.HolderListBinding
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel

class ListViewHolder(val binding: HolderListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: BoardListViewModel, boardData: BoardData) {
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
