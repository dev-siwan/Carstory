package com.like.drive.motorfeed.ui.feed.list.fragment

import android.os.Bundle
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentFeedListBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedListFragment : BaseFragment<FragmentFeedListBinding>(R.layout.fragment_feed_list) {

    private val viewModel: FeedListViewModel by viewModel()

    override fun onBind(dataBinding: FragmentFeedListBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = FeedListAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}