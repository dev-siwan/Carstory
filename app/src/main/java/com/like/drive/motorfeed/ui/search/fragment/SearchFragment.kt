package com.like.drive.motorfeed.ui.search.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.search.adapter.RecentlyListAdapter
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_search_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModel()
    private val feedListViewModel: FeedListViewModel by viewModel()
    private val feedAdapter by lazy { FeedListAdapter(feedListViewModel) }
    private val editFocusListener by lazy {
        View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                visibleSearchView()
            } else {
                goneSearchView()
            }
        }
    }
    private val appbarParams by lazy { etSearch.layoutParams as AppBarLayout.LayoutParams }

    override fun onBind(dataBinding: FragmentSearchBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.feedVm = feedListViewModel
        dataBinding.incSearchList.rvFeed.adapter = feedAdapter
        dataBinding.incRecentlyList.rvRecently.adapter = RecentlyListAdapter(viewModel)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (incRecentlyList.isVisible) {
                goneSearchView()
            }
        }

    }

    private fun initView() {
        rvFeed.apply {
            paging()
        }
        etSearch.onFocusChangeListener = editFocusListener

    }

    private fun RecyclerView.paging() {

        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(feedListViewModel) {
                    if (!getLastDate()) {
                        feedList.value?.lastOrNull()?.createDate?.let {
                            moreData(it)
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        });

    }

    private fun withViewModel() {
        with(viewModel) {
            searchComplete()
        }
        with(feedListViewModel) {
            listComplete()
            pageToDetailAct()
        }
    }

    private fun SearchViewModel.searchComplete() {
        tagValueEvent.observe(viewLifecycleOwner, Observer {
            feedListViewModel.initDate(tagQuery = it)
        })
    }

    private fun FeedListViewModel.listComplete() {
        feedList.observe(viewLifecycleOwner, Observer {
            feedAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
    }

    private fun visibleSearchView() {
        if (!incRecentlyList.isVisible) {
            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 400
                addTarget(incRecentlyList)
            }

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
            incRecentlyList.visibility = View.VISIBLE


            appbarParams.scrollFlags = (AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL)
        }
    }

    private fun goneSearchView() {
        if (incRecentlyList.isVisible) {
            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 200
                addTarget(incRecentlyList)
            }

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
            incRecentlyList.visibility = View.GONE

            coordinator.requestFocus()
            appbarParams.scrollFlags =
                (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS)
        }
    }

    private fun FeedListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startForResult(
                FeedDetailActivity::class,
                FeedListFragment.FEED_LIST_TO_DETAIL_REQ, Bundle().apply {
                    putString(FeedDetailActivity.KEY_FEED_ID, it)
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FeedListFragment.FEED_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    FeedDetailActivity.FEED_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedAdapter.removeFeed(it)
                        }
                    }
                }
            }
        }
    }

}