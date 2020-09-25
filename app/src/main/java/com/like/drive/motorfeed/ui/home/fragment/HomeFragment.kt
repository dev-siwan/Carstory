package com.like.drive.motorfeed.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.motor.MotorTypeList
import com.like.drive.motorfeed.data.user.FilterData
import com.like.drive.motorfeed.databinding.FragmentHomeBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.dividerItemDecoration
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.motorfeed.ui.board.list.activity.BoardListActivity
import com.like.drive.motorfeed.ui.board.list.adapter.BoardListAdapter
import com.like.drive.motorfeed.ui.board.list.viewmodel.BoardListViewModel
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
    private val boardListViewModel: BoardListViewModel by viewModel()
    private val listAdapter by lazy { BoardListAdapter(vm = boardListViewModel) }
    private val emptySnackBar by lazy {
        Snackbar.make(
            requireView(),
            getString(R.string.filter_empty_upload_message),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.white_100))
            setTextColor(ContextCompat.getColor(requireContext(), R.color.gnt_black))
            setActionTextColor(ContextCompat.getColor(requireContext(), R.color.grey_4))
        }
    }

    override fun onBind(dataBinding: FragmentHomeBinding) {
        super.onBind(dataBinding)

        dataBinding.feedVm = boardListViewModel
        dataBinding.vm = viewModel
        dataBinding.rvFeedList.apply {

            adapter = listAdapter
            paging()
            addItemDecoration(dividerItemDecoration())

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    fun initView() {

        swipeLayout.setOnRefreshListener {
            boardListViewModel.loadingStatus = LoadingStatus.REFRESH
            boardListViewModel.initDate(viewModel.category.value, viewModel.motorType.value)
        }

        setOnItemClick()
        initData()

    }

    private fun setOnItemClick() {

        btnCategory.setOnClickListener {
            showCategoryList()
        }

        btnMotorType.setOnClickListener {
            showMotorType()
        }


        btnFilterSearch.setOnClickListener {
            ListFilterDialog.newInstance(
                feedTypeData = viewModel.category.value,
                motorTypeData = viewModel.motorType.value
            ).apply {
                setFilter = { feedType, motorType ->
                    this@HomeFragment.viewModel.setFilterData(feedType, motorType)
                    dismiss()
                }
            }.show(requireActivity().supportFragmentManager, "")
        }
    }

    private fun initData() {
        if (boardListViewModel.isFirstLoad) {

            boardListViewModel.run {
                loadingStatus = LoadingStatus.INIT
                boardListViewModel.initDate(viewModel.category.value, viewModel.motorType.value)
            }

        }
    }

    private fun RecyclerView.paging() {
        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(boardListViewModel) {
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

        with(boardListViewModel) {
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

    private fun BoardListViewModel.completeFeedList() {
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

    private fun BoardListViewModel.initEmpty() {

        initEmpty.observe(viewLifecycleOwner, Observer {
            appBar.setExpanded(!it)
            if (it) {
                emptySnackBar.setAction(
                    "실행"
                ) {
                    (requireActivity() as MainActivity).moveToFilterUploadPage(
                        FilterData(
                            viewModel.category.value,
                            viewModel.motorType.value
                        )
                    )
                }.show()
            } else {
                emptySnackBar.dismiss()
            }
        })

    }

    private fun BoardListViewModel.pageToDetailAct() {
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
            boardListViewModel.loadingStatus = LoadingStatus.INIT
            boardListViewModel.initDate(
                motorTypeData = it.motorType,
                categoryData = it.categoryData
            )
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
                        data?.getParcelableExtra<BoardData>(BoardDetailActivity.KEY_BOARD_DATA)
                            ?.let {
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

                                boardListViewModel.run {

                                    val categoryData =
                                        getCategoryList(requireContext()).find { code -> code.typeCode == it.categoryCode }
                                    val motorTypeData =
                                        MotorTypeList().motorTypeList.find { code -> code.brandCode == it.brandCode && code.modelCode == it.modelCode }

                                    viewModel.setFilterData(categoryData, motorTypeData)
                                }
                            }
                    }
                }
            }

            SelectMotorTypeActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                            viewModel.setFilterData(viewModel.category.value, it)
                        }
                }
            }

        }

    }

}

