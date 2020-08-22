package com.like.drive.motorfeed.ui.feed.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListViewHolder
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel

class FeedListAdapter(val vm: FeedListViewModel) : RecyclerView.Adapter<FeedListViewHolder>() {

    val feedList = mutableListOf<FeedData>().apply {
        sortByDescending { it.updateDate }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        FeedListViewHolder.from(parent)

    override fun getItemCount() = feedList.size

    override fun onBindViewHolder(holder: FeedListViewHolder, position: Int) {
        holder.bind(vm, feedList[position])
    }

    fun initList(feedList: List<FeedData>){
        this.feedList.run {
            clear()
            addAll(feedList)
            notifyDataSetChanged()
        }

    }
    fun addAll(feedList: List<FeedData>) {
        this.feedList.addAll(feedList)
        notifyDataSetChanged()
    }

    fun addFeed(feed:FeedData){
        feedList.add(feed)
        notifyItemInserted(feedList.size-1)
    }

    fun updateFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList[index] = feed
            notifyItemChanged(index)
        }
    }

    fun removeFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}