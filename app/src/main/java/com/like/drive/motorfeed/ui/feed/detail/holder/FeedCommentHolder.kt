package com.like.drive.motorfeed.ui.feed.detail.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.databinding.HolderFeedCommentBinding
import com.like.drive.motorfeed.ui.feed.detail.adapter.ReCommentAdapter
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel

class FeedCommentHolder(val binding: HolderFeedCommentBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var reCommentAdapter: ReCommentAdapter? = null

    fun bind(vm: FeedDetailViewModel, data: CommentWrapData) {

        reCommentAdapter = ReCommentAdapter(vm)
        binding.vm = vm
        binding.commentData = data.commentData

        binding.tvReComment.setOnClickListener {
            vm.showCommentDialogListener(false,data.commentData)
        }

        binding.rvReComment.run {
            adapter = reCommentAdapter?.apply {
                reCommentList.addAll(data.reCommentList)
            }
        }

        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): FeedCommentHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderFeedCommentBinding.inflate(layoutInflater, parent, false)

            return FeedCommentHolder(binding)
        }
    }
}