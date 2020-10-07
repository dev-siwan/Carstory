package com.like.drive.carstory.ui.board.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.board.ReCommentData
import com.like.drive.carstory.databinding.HolderReCommentBinding
import com.like.drive.carstory.ui.board.detail.viewmodel.BoardDetailViewModel

class ReCommentHolder(val binding: HolderReCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm :BoardDetailViewModel, data: ReCommentData) {
        binding.vm = vm
        binding.reCommentData = data
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ReCommentHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderReCommentBinding.inflate(layoutInflater, parent, false)

            return ReCommentHolder(binding)
        }
    }
}