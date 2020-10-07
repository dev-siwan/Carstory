package com.like.drive.carstory.ui.home.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.R
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.data.motor.MotorTypeList
import com.like.drive.carstory.data.user.FilterData
import com.like.drive.carstory.databinding.FragmentHomeBinding
import com.like.drive.carstory.ui.base.BaseFragment
import com.like.drive.carstory.ui.base.etc.PagingCallback
import com.like.drive.carstory.ui.base.ext.dividerItemDecoration
import com.like.drive.carstory.ui.base.ext.showListDialog
import com.like.drive.carstory.ui.base.ext.startActForResult
import com.like.drive.carstory.ui.base.ext.withPaging
import com.like.drive.carstory.ui.board.category.data.CategoryData
import com.like.drive.carstory.ui.board.category.data.getCategoryList
import com.like.drive.carstory.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.carstory.ui.board.list.activity.BoardListActivity
import com.like.drive.carstory.ui.board.list.adapter.BoardListAdapter
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel
import com.like.drive.carstory.ui.board.upload.activity.UploadActivity
import com.like.drive.carstory.ui.common.data.LoadingStatus
import com.like.drive.carstory.ui.home.dialog.EmptyFilterDialog
import com.like.drive.carstory.ui.home.viewmodel.HomeViewModel
import com.like.drive.carstory.ui.main.activity.MainActivity
import com.like.drive.carstory.ui.motor.activity.SelectMotorTypeActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_home_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel: HomeViewModel by viewModel()
    private val boardListViewModel: BoardListViewModel by viewModel()
    private val listAdapter by lazy { BoardListAdapter(vm = boardListViewModel) }
    private var emptyFilterDialog: EmptyFilterDialog? = null

    override fun onBind(dataBinding: FragmentHomeBinding) {
        super.onBind(dataBinding)

        dataBinding.boardVm = boardListViewModel
        dataBinding.vm = viewModel
        dataBinding.rvBoardList.apply {

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
            boardListViewModel.initData(viewModel.category.value, viewModel.motorType.value)
        }

        emptyFilterDialog = EmptyFilterDialog.newInstance()

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

    }

    private fun initData() {
        if (boardListViewModel.isFirstLoad) {

            if (listAdapter.boardList.isEmpty()) {
                boardListViewModel.run {
                    loadingStatus = LoadingStatus.INIT
                    boardListViewModel.initData(viewModel.category.value, viewModel.motorType.value)
                }

            }
        }
    }

    private fun RecyclerView.paging() {
        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(boardListViewModel) {
                    if (!getLastDate()) {
                        boardList.value?.lastOrNull()?.let {
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
            completeBoardList()
            pageToDetailAct()
            initEmpty()
        }
    }

    private fun HomeViewModel.searchClick() {
        moveSearchEvent.observe(viewLifecycleOwner, Observer {
            (requireActivity() as MainActivity).navBottomView.selectedItemId = R.id.action_search
        })
    }

    private fun BoardListViewModel.completeBoardList() {
        boardList.observe(viewLifecycleOwner, Observer {
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

        initEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->

            if (isEmpty == true) {
                if (emptyFilterDialog?.isVisible == false) {
                    emptyFilterDialog?.apply {
                        registerAction = {
                            (requireActivity() as MainActivity).moveToFilterUploadPage(
                                FilterData(
                                    viewModel.category.value,
                                    viewModel.motorType.value
                                )
                            )
                            dismiss()
                        }
                    }?.show(requireActivity().supportFragmentManager, "")

                    appBar.setExpanded(true)
                }
            } else {
                emptyFilterDialog?.run {
                    if (isVisible) {
                        dismiss()
                    }
                }
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
            boardListViewModel.initData(
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

                val category = if (position == 0) null else it[position]

                viewModel.setFilterData(category, viewModel.motorType.value)

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
                                listAdapter.updateBoard(it)
                            }
                    }
                    BoardDetailActivity.BOARD_REMOVE_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            listAdapter.removeBoard(it)
                        }
                    }
                    BoardDetailActivity.BOARD_NOT_FOUND_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            listAdapter.removeBoard(it)
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
                                    // listAdapter.addBoard(it)
                                    initEmptyValue(false)
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

