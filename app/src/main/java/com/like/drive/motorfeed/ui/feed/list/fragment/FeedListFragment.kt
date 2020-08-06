package com.like.drive.motorfeed.ui.feed.list.fragment

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentFeedListBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import kotlinx.android.synthetic.main.fragment_feed_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedListFragment : BaseFragment<FragmentFeedListBinding>(R.layout.fragment_feed_list) {

    private val viewModel: FeedListViewModel by viewModel()

    override fun onBind(dataBinding: FragmentFeedListBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = FeedListAdapter(viewModel)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    private fun initView() {
        val decorationItem = DividerItemDecoration(
            requireContext(), DividerItemDecoration.VERTICAL
        ).apply {
            ContextCompat.getDrawable(
                requireContext(), R.drawable.line_solid_grey_6
            )?.let { setDrawable(it) }
        }
        rvFeedList?.run {
            addItemDecoration(decorationItem)
        }

    }

    private fun withViewModel() {
        with(viewModel) {
            pageToDetailAct()
        }
    }

    private fun FeedListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startAct(FeedDetailActivity::class, Bundle().apply {
                putString(FeedDetailActivity.KEY_FEED_ID, it)
            })
        })
    }
}
