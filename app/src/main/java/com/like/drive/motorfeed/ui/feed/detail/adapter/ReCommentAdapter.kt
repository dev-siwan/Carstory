package com.like.drive.motorfeed.ui.feed.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.ui.feed.detail.holder.FeedReCommentHolder
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel

class ReCommentAdapter(val vm: FeedDetailViewModel) : RecyclerView.Adapter<FeedReCommentHolder>() {

    var reCommentList = mutableListOf<ReCommentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedReCommentHolder.from(parent)

    override fun onBindViewHolder(holder: FeedReCommentHolder, position: Int) {
        holder.bind(vm,reCommentList[position])
    }

    override fun getItemCount() = reCommentList.size

}

