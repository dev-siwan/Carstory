package com.like.drive.motorfeed.ui.board.detail.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.board.ReCommentData
import com.like.drive.motorfeed.ui.board.detail.holder.ReCommentHolder
import com.like.drive.motorfeed.ui.board.detail.viewmodel.BoardDetailViewModel

class ReCommentAdapter(val vm: BoardDetailViewModel) : RecyclerView.Adapter<ReCommentHolder>() {

    var reCommentList = mutableListOf<ReCommentData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReCommentHolder.from(parent)

    override fun onBindViewHolder(holder: ReCommentHolder, position: Int) {
        holder.bind(vm,reCommentList[position])
    }

    override fun getItemCount() = reCommentList.size

}

