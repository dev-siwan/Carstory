package com.like.drive.motorfeed.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.FragmentHomeBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.home.adapter.HomeAdapter
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_home_content.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()
    private val feedListViewModel: FeedListViewModel by viewModel()
    private val mainVm : MainViewModel by sharedViewModel()
    private val homeListAdapter by lazy { HomeAdapter(viewModel, feedListViewModel) }

    override fun onBind(dataBinding: FragmentHomeBinding) {
        super.onBind(dataBinding)

        dataBinding.mainVm = mainVm
        dataBinding.vm = viewModel
        dataBinding.incContent.rvFeedList.adapter = homeListAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    fun initView() {

        rvFeedList.apply {

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

            })

            scrollToPosition(0)
        }


        initData()
    }

    private fun initData() {
        if (homeListAdapter.feedList.isEmpty()) {
            feedListViewModel.initDate(null, null, null)
        }
    }

    fun withViewModel() {
        with(feedListViewModel) {
            completeFeedList()
            pageToDetailAct()
        }
        with(viewModel){
            searchClick()
        }
    }

    private fun HomeViewModel.searchClick(){
        moveSearchEvent.observe(viewLifecycleOwner, Observer {
            (requireActivity() as MainActivity).navBottomView.selectedItemId=R.id.action_search
        })
    }

    private fun FeedListViewModel.completeFeedList() {
        feedList.observe(viewLifecycleOwner, Observer {
            homeListAdapter.run {
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
                            homeListAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            homeListAdapter.removeFeed(it)
                        }
                    }
                }
            }
/*
            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<FeedData>(FeedUploadActivity.FEED_CREATE_KEY)
                            ?.let {
                                homeListAdapter.run {
                                    addFeed(it)
                                }
                            }
                    }
                }
            }*/
        }
    }

}