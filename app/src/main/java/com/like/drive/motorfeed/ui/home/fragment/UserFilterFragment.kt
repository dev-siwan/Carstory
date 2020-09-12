package com.like.drive.motorfeed.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.FragmentUserFilterBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.home.adapter.HomeFeedAdapter
import com.like.drive.motorfeed.ui.home.data.HomeTab
import com.like.drive.motorfeed.ui.home.viewmodel.UserFilterViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import kotlinx.android.synthetic.main.fragment_news_feed.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserFilterFragment : BaseFragment<FragmentUserFilterBinding>(R.layout.fragment_user_filter) {

    private val feedListViewModel: FeedListViewModel by viewModel()
    val viewModel: UserFilterViewModel by viewModel()

    private val listAdapter by lazy {
        HomeFeedAdapter(
            viewModel = viewModel,
            feedListViewModel = feedListViewModel,
            homeTab = HomeTab.FILTER
        )
    }
    private val filterDialog by lazy { FeedListFilterDialog }

    override fun onBind(dataBinding: FragmentUserFilterBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.feedVm = feedListViewModel
        dataBinding.rvFeedList.adapter = listAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    private fun initView() {
        rvFeedList.apply {
            paging()
        }

        swipeLayout.setOnRefreshListener {
            feedListViewModel.loadingStatus = FeedListViewModel.LoadingStatus.REFRESH
            feedListViewModel.initDate(
                feedTypeData = viewModel.feedType.value,
                motorTypeData = viewModel.motorType.value
            )
        }

    }

    private fun RecyclerView.paging() {
        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(feedListViewModel) {
                    if (!getLastDate()) {
                        feedList.value?.lastOrNull()?.let {
                            moreData(
                                date = it.createDate
                            )
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })

        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                ContextCompat.getDrawable(requireContext(), R.drawable.divider_feed_list)?.let {
                    setDrawable(it)
                }
            }

        addItemDecoration(dividerItemDecoration)
    }

    fun withViewModel() {
        with(feedListViewModel) {
            completeFeedList()
            pageToDetailAct()
        }

        with(viewModel) {
            showFilterDialog()
            setFilter()
        }

    }

    private fun UserFilterViewModel.setFilter() {
        setUserFilterEvent.observe(viewLifecycleOwner, Observer {
            feedListViewModel.loadingStatus = FeedListViewModel.LoadingStatus.INIT
            feedListViewModel.initDate(motorTypeData = it.motorType, feedTypeData = it.feedType)
        })
    }

    private fun UserFilterViewModel.showFilterDialog() {
        filterClickEvent.observe(viewLifecycleOwner, Observer {
            val filterDialog = it?.let {
                filterDialog.newInstance(it.feedType, it.motorType)
            } ?: filterDialog.newInstance(null, null)

            filterDialog.apply {
                setFilter = { feedType, motorType ->
                    this@UserFilterFragment.viewModel.setFilter(feedType, motorType)
                    dismiss()
                }
            }.show(requireActivity().supportFragmentManager, "")
        })
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
                        data?.getStringExtra(FeedDetailActivity.KEY_FEED_DATA)?.let {
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