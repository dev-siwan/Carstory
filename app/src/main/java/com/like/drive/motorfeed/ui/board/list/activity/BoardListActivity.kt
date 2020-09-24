package com.like.drive.motorfeed.ui.board.list.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.databinding.ActivityBoardListBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.motorfeed.ui.board.list.adapter.BoardListAdapter
import com.like.drive.motorfeed.ui.board.list.viewmodel.BoardListViewModel
import com.like.drive.motorfeed.ui.board.upload.activity.UploadActivity
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import kotlinx.android.synthetic.main.activity_board_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardListActivity : BaseActivity<ActivityBoardListBinding>(R.layout.activity_board_list) {

    private val viewModel: BoardListViewModel by viewModel()
    private val boardListAdapter by lazy { BoardListAdapter(viewModel) }
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        withViewModel()

    }

    override fun onBinding(dataBinding: ActivityBoardListBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = boardListAdapter
    }

    private fun initView() {
        val decorationItem = DividerItemDecoration(
            this, DividerItemDecoration.VERTICAL
        ).apply {
            ContextCompat.getDrawable(
                this@BoardListActivity, R.drawable.line_solid_grey_6
            )?.let { setDrawable(it) }
        }
        rvFeedList?.run {
            addItemDecoration(decorationItem)
            paging()
        }

        setCloseButtonToolbar(toolbar) {
            finish()

        }

        intent.getParcelableExtra<UserData>(BOARD_DATE_KEY)?.let {
            uid = it.uid
            viewModel.loadingStatus = LoadingStatus.INIT
            initData(uid)
            tvTitle.text = getString(R.string.my_feed_title_format, it.nickName)
        }

        swipeLayout.setOnRefreshListener {
            uid?.let {
                viewModel.loadingStatus = LoadingStatus.REFRESH
                viewModel.initUserData(it)
            }
        }

    }

    private fun RecyclerView.paging() {

        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(viewModel) {
                    if (!getLastDate()) {
                        feedList.value?.lastOrNull()?.createDate?.let { date ->
                            uid?.let { moreUserData(date, it) }
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })

    }

    private fun initData(uid: String?) {
        uid?.let { viewModel.initUserData(it) }
    }

    private fun withViewModel() {
        with(viewModel) {
            pageToDetailAct()
            completeFeedList()
        }

    }

    private fun BoardListViewModel.completeFeedList() {
        feedList.observe(this@BoardListActivity, Observer {
            boardListAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
    }

    private fun BoardListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(this@BoardListActivity, Observer {
            startActForResult(BoardDetailActivity::class, BOARD_LIST_TO_DETAIL_REQ, Bundle().apply {
                putString(BoardDetailActivity.KEY_BOARD_ID, it)
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            BOARD_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    BoardDetailActivity.BOARD_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<BoardData>(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            boardListAdapter.updateFeed(it)
                        }
                    }
                    BoardDetailActivity.BOARD_REMOVE_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            boardListAdapter.removeFeed(it)
                        }
                    }
                    BoardDetailActivity.BOARD_NOT_FOUND_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {

                            boardListAdapter.getFeedData(it)?.let { feedData ->
                                viewModel.removeFeed(feedData)
                            }

                            boardListAdapter.removeFeed(it)

                        }
                    }
                }
            }

            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<BoardData>(UploadActivity.BOARD_CREATE_KEY)
                            ?.let {
                                boardListAdapter.run {
                                    addFeed(it)
                                }
                            }
                    }
                }
            }
        }
    }

    companion object {
        const val BOARD_LIST_TO_DETAIL_REQ = 1515
        const val BOARD_DATE_KEY = "boardDataKey"
    }
}
