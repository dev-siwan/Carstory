package com.like.drive.motorfeed.ui.feed.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListViewHolder

class FeedListAdapter : RecyclerView.Adapter<FeedListViewHolder>() {

    var feedList = mutableListOf<FeedData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedListViewHolder.from(parent)

    override fun getItemCount() = feedList.size

    override fun onBindViewHolder(holder: FeedListViewHolder, position: Int) {
        holder.bind(feedList[position])
    }

    fun addAll(feedList:List<FeedData>){
        this.feedList.addAll(feedList)
        notifyDataSetChanged()
    }
}