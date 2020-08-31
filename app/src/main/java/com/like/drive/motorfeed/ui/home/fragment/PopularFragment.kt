package com.like.drive.motorfeed.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.FragmentNewsFeedBinding
import com.like.drive.motorfeed.databinding.FragmentPopularBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.home.adapter.NewsFeedAdapter
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_news_feed.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class PopularFragment : BaseFragment<FragmentPopularBinding>(R.layout.fragment_popular) {

    private val feedListViewModel: FeedListViewModel by viewModel()
    val viewModel: HomeViewModel by viewModel()

    private val listAdapter by lazy { FeedListAdapter(feedListViewModel) }

    override fun onBind(dataBinding: FragmentPopularBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = listAdapter

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
                            feedList.value?.lastOrNull()?.likeCount?.let {
                                moreData(likeCount = it,isPopular = true)
                            }
                        }
                    }
                }

                override fun isRequest(): Boolean = false

            })
        }

    }

    private fun initData() {
        if (listAdapter.feedList.isEmpty()) {
            feedListViewModel.initDate(isPopular = true)
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
            listAdapter.run {
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
                            listAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            listAdapter.removeFeed(it)
                        }
                    }
                }
            }

            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<FeedData>(FeedUploadActivity.FEED_CREATE_KEY)
                            ?.let {
                                listAdapter.run {
                                    addFeed(it)
                                }
                            }
                    }
                }
            }
        }

    }
}