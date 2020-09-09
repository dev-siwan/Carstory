package com.like.drive.motorfeed.ui.feed.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListAdvHolder
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListViewHolder
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.home.adapter.HomeFeedAdapter

class FeedListAdapter(val vm: FeedListViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val feedList = mutableListOf<FeedData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADV -> FeedListAdvHolder.from(parent)
            else -> FeedListViewHolder.from(parent)
        }
    }

    override fun getItemCount() =
        feedList.size + feedList.size.div(HomeFeedAdapter.FEED_ADV_POSITION)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FeedListViewHolder -> holder.bind(
                vm,
                feedList[position - position.div(5)]
            )
            is FeedListAdvHolder -> holder.bind()
        }
    }

    fun initList(feedList: List<FeedData>) {
        this.feedList.run {
            clear()
            addAll(feedList)
            notifyDataSetChanged()
        }
    }

    fun moreList(feedList: List<FeedData>) {
        this.feedList.run {
            val beforePosition = size + size.div(HomeFeedAdapter.FEED_ADV_POSITION)
            addAll(feedList)
            notifyItemRangeInserted(beforePosition, feedList.size + beforePosition)
        }
    }

    fun addFeed(feed: FeedData) {
        feedList.add(feed)
        notifyItemInserted(feedList.size - 1)
    }

    fun updateFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList[index] = feed
            val advIndex = index.div(HomeFeedAdapter.FEED_ADV_POSITION)
            notifyItemChanged((index + advIndex))
        }
    }

    fun removeFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList.removeAt(index)
            val advIndex = index.div(HomeFeedAdapter.FEED_ADV_POSITION)
            notifyItemRemoved((index + advIndex))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_ITEM
            position.rem(HomeFeedAdapter.FEED_ADV_POSITION) == 0 -> TYPE_ADV
            else -> TYPE_ITEM
        }
    }

    companion object {
        const val TYPE_ADV = 1
        const val TYPE_ITEM = 0
    }
}