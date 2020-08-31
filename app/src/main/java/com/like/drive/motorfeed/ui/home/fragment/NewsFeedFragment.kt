package com.like.drive.motorfeed.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.FragmentNewsFeedBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.home.adapter.NewsFeedAdapter
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_news_feed.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFeedFragment : BaseFragment<FragmentNewsFeedBinding>(R.layout.fragment_news_feed) {

    private val feedListViewModel: FeedListViewModel by viewModel()
    val viewModel: HomeViewModel by viewModel()

    private val newsFeedAdapter by lazy { NewsFeedAdapter(viewModel, feedListViewModel) }

    override fun onBind(dataBinding: FragmentNewsFeedBinding) {
        super.onBind(dataBinding)

        dataBinding.feedVm = feedListViewModel
        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = newsFeedAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initView()
        withViewModel()
    }

    private fun initView() {
        rvFeedList.apply {

            withPaging(object : PagingCallback {
                override fun requestMoreList() {

                    with(feedListViewModel) {
                        if (!getLastDate()) {
                            feedList.value?.lastOrNull()?.createDate?.let {
                                moreData(date = it)
                            }
                        }
                    }
                }

                override fun isRequest(): Boolean = false

            })
        }


        swipeLayout.setOnRefreshListener {
            newsFeedAdapter.feedList.clear()
            feedListViewModel.isRefresh.set(true)
            feedListViewModel.initDate()
            rvFeedList.smoothScrollToPosition(0)
        }

    }

    private fun initData() {
        if (newsFeedAdapter.feedList.isEmpty()) {
            feedListViewModel.initDate()
        }
    }

    fun withViewModel() {
        with(feedListViewModel) {
            completeFeedList()
            pageToDetailAct()
        }

    }

    private fun FeedListViewModel.completeFeedList() {
        feedList.observe(viewLifecycleOwner, Observer {
            newsFeedAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
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
                            newsFeedAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            newsFeedAdapter.removeFeed(it)
                        }
                    }
                }
            }

            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<FeedData>(FeedUploadActivity.FEED_CREATE_KEY)
                            ?.let {
                                newsFeedAdapter.run {
                                    addFeed(it)
                                }
                            }
                    }
                }
            }
        }

    }
}