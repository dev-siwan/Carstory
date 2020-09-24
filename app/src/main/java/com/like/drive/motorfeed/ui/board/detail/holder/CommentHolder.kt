package com.like.drive.motorfeed.ui.board.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.board.CommentWrapData
import com.like.drive.motorfeed.databinding.HolderCommentBinding
import com.like.drive.motorfeed.ui.board.detail.adapter.ReCommentAdapter
import com.like.drive.motorfeed.ui.board.detail.viewmodel.BoardDetailViewModel

class CommentHolder(val binding: HolderCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var reCommentAdapter: ReCommentAdapter? = null

    fun bind(vm: BoardDetailViewModel, data: CommentWrapData) {

        reCommentAdapter = ReCommentAdapter(vm)
        binding.vm = vm
        binding.commentData = data.commentData

        binding.tvReComment.setOnClickListener {
            vm.showCommentDialogListener(false, data.commentData)
        }

        binding.rvReComment.run {
            adapter = reCommentAdapter?.apply {
                reCommentList.addAll(data.reCommentList)
            }
        }

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CommentHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderCommentBinding.inflate(layoutInflater, parent, false)

            return CommentHolder(binding)
        }
    }
}