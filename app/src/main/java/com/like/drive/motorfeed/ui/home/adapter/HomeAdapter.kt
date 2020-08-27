package com.like.drive.motorfeed.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListViewHolder
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.home.holder.HomeFeedListDescViewHolder
import com.like.drive.motorfeed.ui.home.holder.HomeRegisterFeedViewHolder
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel

class HomeAdapter(
    private val homeViewModel: HomeViewModel,
    private val feedListViewModel: FeedListViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val feedList = mutableListOf<FeedData>().apply {
        sortByDescending { it.updateDate }
    }

    private val TYPE_REGISTER_FEED = 0
    private val TYPE_TITLE = 1
    private val TYPE_ITEM = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_REGISTER_FEED -> HomeRegisterFeedViewHolder.from(parent)
            TYPE_TITLE -> HomeFeedListDescViewHolder.from(parent)
            else -> FeedListViewHolder.from(parent)
        }
    }

    override fun getItemCount() = feedList.size + FEED_LIST_START_POSITION

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HomeRegisterFeedViewHolder -> holder.bind(homeViewModel)
            is FeedListViewHolder -> holder.bind(feedListViewModel, feedList[position - FEED_LIST_START_POSITION])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            TYPE_REGISTER_FEED -> TYPE_REGISTER_FEED
            TYPE_TITLE -> TYPE_TITLE
            else -> TYPE_ITEM
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
            val beforePosition = (size + FEED_LIST_START_POSITION)
            addAll(feedList)
            notifyItemRangeInserted(beforePosition, feedList.size + beforePosition)
        }
    }

    fun addFeed(feed: FeedData) {
        feedList.add(feed)
        notifyItemInserted(0)
    }

    fun updateFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList[index] = feed
            notifyItemChanged(index + FEED_LIST_START_POSITION)
        }
    }

    fun removeFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList.removeAt(index)
            notifyItemRemoved(index + FEED_LIST_START_POSITION)
        }
    }

    companion object{
        const val FEED_LIST_START_POSITION = 2
    }
}