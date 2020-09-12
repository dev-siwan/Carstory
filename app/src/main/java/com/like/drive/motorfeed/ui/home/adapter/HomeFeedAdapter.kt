package com.like.drive.motorfeed.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListAdvHolder
import com.like.drive.motorfeed.ui.feed.list.holder.FeedListViewHolder
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.home.data.HomeTab
import com.like.drive.motorfeed.ui.home.holder.NewsFeedHeaderViewHolder
import com.like.drive.motorfeed.ui.home.holder.UserFilterHeaderViewHolder
import com.like.drive.motorfeed.ui.home.viewmodel.NewsFeedViewModel
import com.like.drive.motorfeed.ui.home.viewmodel.UserFilterViewModel

class HomeFeedAdapter(
    private val viewModel: BaseViewModel? = null,
    private val feedListViewModel: FeedListViewModel,
    private val homeTab: HomeTab
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val feedList = mutableListOf<FeedData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                when (homeTab) {
                    HomeTab.NEWS_FEED -> NewsFeedHeaderViewHolder.from(parent)
                    else -> UserFilterHeaderViewHolder.from(parent)
                }
            }
            TYPE_ADV -> FeedListAdvHolder.from(parent)
            else -> FeedListViewHolder.from(parent)
        }
    }

    override fun getItemCount() =
        (feedList.size + feedList.size.div(FEED_ADV_POSITION) + FEED_LIST_START_POSITION)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NewsFeedHeaderViewHolder -> holder.bind((viewModel as NewsFeedViewModel))
            is UserFilterHeaderViewHolder -> holder.bind((viewModel as UserFilterViewModel))
            is FeedListViewHolder -> holder.bind(
                feedListViewModel,
                feedList[(position - position.div(5)) - FEED_LIST_START_POSITION]
            )
            is FeedListAdvHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_HEADER
            position == 1 -> TYPE_ITEM
            position.rem(FEED_ADV_POSITION) == 0 -> TYPE_ADV
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
            val beforePosition = (size + size.div(FEED_ADV_POSITION)) + FEED_LIST_START_POSITION
            addAll(feedList)
            notifyItemRangeInserted(beforePosition, feedList.size + beforePosition)
        }

    }

    fun addFeed(feed: FeedData) {
        feedList.add(feed)
        notifyItemInserted(FEED_LIST_START_POSITION)
    }

    fun updateFeed(feed: FeedData) {
        val originData = feedList.find { it.fid == feed.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList[index] = feed
            val advIndex = index.div(FEED_ADV_POSITION)
            notifyItemChanged((index + advIndex) + FEED_LIST_START_POSITION)
        }
    }

    fun removeFeed(fid: String) {
        val originData = feedList.find { it.fid == fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList.removeAt(index)
            val advIndex = index.div(FEED_ADV_POSITION)
            notifyItemRemoved((index + advIndex) + FEED_LIST_START_POSITION)
        }
    }

    companion object {
        //Position
        const val FEED_LIST_START_POSITION = 1
        const val FEED_ADV_POSITION = 5

        //TYPE
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
        const val TYPE_ADV = 2
    }

}