package com.like.drive.motorfeed.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.FragmentHomeBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.activity.FeedListActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.type.data.getFeedTypeList
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_home_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()
    private val feedVM: FeedListViewModel by viewModel()
    private val listAdapter by lazy { FeedListAdapter(vm = feedVM) }
    private val filterDialog by lazy {
        FeedListFilterDialog.newInstance(
            feedTypeData = viewModel.feedType.value,
            motorTypeData = viewModel.motorType.value
        )
    }

    override fun onBind(dataBinding: FragmentHomeBinding) {
        super.onBind(dataBinding)
        dataBinding.feedVm = feedVM
        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = listAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    fun initView() {

        rvFeedList.run {
            paging()

            val dividerItemDecoration =
                DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider_feed_list)?.let {
                        setDrawable(it)
                    }
                }

            addItemDecoration(dividerItemDecoration)
        }

        swipeLayout.setOnRefreshListener {
            feedVM.loadingStatus = LoadingStatus.REFRESH
            feedVM.initDate(viewModel.feedType.value, viewModel.motorType.value)
        }

        setOnItemClick()

        initData()

        btnFilterSearch.setOnClickListener {
            filterDialog.apply {
                setFilter = { feedType, motorType ->
                    viewModel.setFilter(feedType, motorType)
                    dismiss()
                }
            }.show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun setOnItemClick() {

        tvFeedType.setOnClickListener {
            showFeedTypeList()
        }

        tvMotorType.setOnClickListener {
            showMotorType()
        }
    }

    private fun initData() {
        if (feedVM.isFirstLoad) {

            feedVM.run {
                loadingStatus = LoadingStatus.INIT
                feedVM.initDate(viewModel.feedType.value, viewModel.motorType.value)
            }
        }
    }

    private fun RecyclerView.paging() {
        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(feedVM) {
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
    }

    private fun withViewModel() {
        with(viewModel) {
            searchClick()
            setFilter()
        }

        with(feedVM) {
            completeFeedList()
            pageToDetailAct()
            initEmpty()
        }
    }

    private fun HomeViewModel.searchClick() {
        moveSearchEvent.observe(viewLifecycleOwner, Observer {
            (requireActivity() as MainActivity).navBottomView.selectedItemId = R.id.action_search
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

    private fun FeedListViewModel.initEmpty() {

        initEmpty.observe(viewLifecycleOwner, Observer {

            appBar.setExpanded(!it)
        })

    }

    private fun FeedListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startForResult(
                FeedDetailActivity::class,
                FeedListActivity.FEED_LIST_TO_DETAIL_REQ, Bundle().apply {
                    putString(FeedDetailActivity.KEY_FEED_ID, it)
                })
        })
    }

    private fun HomeViewModel.setFilter() {
        setFilterEvent.observe(viewLifecycleOwner, Observer {
            feedVM.loadingStatus = LoadingStatus.INIT
            feedVM.initDate(motorTypeData = it.motorType, feedTypeData = it.feedType)
        })
    }

    private fun showFeedTypeList() {
        getFeedTypeList(requireContext()).toMutableList().apply {
            add(0, FeedTypeData(getString(R.string.all), "", 0))
        }.let {
            requireActivity().showListDialog(it.map { data -> data.title }
                .toTypedArray()) { position ->

                val feedType = if (position == 0) null else it[position]

                viewModel.setFilterData(feedType, viewModel.motorType.value)

            }
        }
    }

    private fun showMotorType() {
        startActForResult(SelectMotorTypeActivity::class, SelectMotorTypeActivity.REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FeedListActivity.FEED_LIST_TO_DETAIL_REQ -> {
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
                    FeedDetailActivity.FEED_NOT_FOUND_RES_CODE -> {
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

            SelectMotorTypeActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                            viewModel.setFilterData(viewModel.feedType.value, it)
                        }
                }
            }

        }

    }

}

