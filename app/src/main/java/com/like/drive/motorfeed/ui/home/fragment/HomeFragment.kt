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
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.FragmentHomeBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.motorfeed.ui.board.list.activity.BoardListActivity
import com.like.drive.motorfeed.ui.board.list.adapter.BoardListAdapter
import com.like.drive.motorfeed.ui.board.list.viewmodel.ListViewModel
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import com.like.drive.motorfeed.ui.board.category.data.getCategoryList
import com.like.drive.motorfeed.ui.board.upload.activity.UploadActivity
import com.like.drive.motorfeed.ui.filter.dialog.ListFilterDialog
import com.like.drive.motorfeed.ui.home.viewmodel.HomeViewModel
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_home_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()
    private val VM: ListViewModel by viewModel()
    private val listAdapter by lazy { BoardListAdapter(vm = VM) }
    private val filterDialog by lazy {
        ListFilterDialog.newInstance(
            feedTypeData = viewModel.feedType.value,
            motorTypeData = viewModel.motorType.value
        )
    }

    override fun onBind(dataBinding: FragmentHomeBinding) {
        super.onBind(dataBinding)
        dataBinding.feedVm = VM
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
                    ContextCompat.getDrawable(requireContext(), R.drawable.divider_board_list)?.let {
                        setDrawable(it)
                    }
                }

            addItemDecoration(dividerItemDecoration)
        }

        swipeLayout.setOnRefreshListener {
            VM.loadingStatus = LoadingStatus.REFRESH
            VM.initDate(viewModel.feedType.value, viewModel.motorType.value)
        }

        setOnItemClick()

        initData()

        btnFilterSearch.setOnClickListener {
            filterDialog.apply {
                setFilter = { feedType, motorType ->
                    this@HomeFragment.viewModel.setFilterData(feedType, motorType)
                    dismiss()
                }
            }.show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun setOnItemClick() {

        tvCategory.setOnClickListener {
            showCategoryList()
        }

        tvMotorType.setOnClickListener {
            showMotorType()
        }
    }

    private fun initData() {
        if (VM.isFirstLoad) {

            VM.run {
                loadingStatus = LoadingStatus.INIT
                VM.initDate(viewModel.feedType.value, viewModel.motorType.value)
            }
        }
    }

    private fun RecyclerView.paging() {
        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(VM) {
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

        with(VM) {
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

    private fun ListViewModel.completeFeedList() {
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

    private fun ListViewModel.initEmpty() {

        initEmpty.observe(viewLifecycleOwner, Observer {
            appBar.setExpanded(!it)
        })

    }

    private fun ListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startForResult(
                BoardDetailActivity::class,
                BoardListActivity.BOARD_LIST_TO_DETAIL_REQ, Bundle().apply {
                    putString(BoardDetailActivity.KEY_BOARD_ID, it)
                })
        })
    }

    private fun HomeViewModel.setFilter() {
        setFilterEvent.observe(viewLifecycleOwner, Observer {
            VM.loadingStatus = LoadingStatus.INIT
            VM.initDate(motorTypeData = it.motorType, categoryData = it.feedType)
        })
    }

    private fun showCategoryList() {
        getCategoryList(requireContext()).toMutableList().apply {
            add(0, CategoryData(getString(R.string.all), "", 0))
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
            BoardListActivity.BOARD_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    BoardDetailActivity.BOARD_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<BoardData>(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            listAdapter.updateFeed(it)
                        }
                    }
                    BoardDetailActivity.BOARD_REMOVE_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            listAdapter.removeFeed(it)
                        }
                    }
                    BoardDetailActivity.BOARD_NOT_FOUND_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            listAdapter.removeFeed(it)
                        }
                    }
                }
            }

            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<BoardData>(UploadActivity.BOARD_CREATE_KEY)
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

