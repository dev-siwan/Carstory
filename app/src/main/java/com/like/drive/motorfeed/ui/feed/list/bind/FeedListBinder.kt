package com.like.drive.motorfeed.ui.feed.list.bind

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter

@BindingAdapter("feedList")
fun RecyclerView.setFeedList(feedList:List<FeedData>?){
    feedList?.let {
        (adapter as FeedListAdapter).addAll(it)
    }
}