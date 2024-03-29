package com.like.drive.carstory.ui.board.list.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.R
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.databinding.ActivityBoardListBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.etc.PagingCallback
import com.like.drive.carstory.ui.base.ext.dividerItemDecoration
import com.like.drive.carstory.ui.base.ext.startActForResult
import com.like.drive.carstory.ui.base.ext.withPaging
import com.like.drive.carstory.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.carstory.ui.board.list.adapter.BoardListAdapter
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel
import com.like.drive.carstory.ui.board.upload.activity.UploadActivity
import com.like.drive.carstory.ui.common.data.LoadingStatus
import com.like.drive.carstory.ui.main.activity.MainActivity
import kotlinx.android.synthetic.main.activity_board_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardListActivity : BaseActivity<ActivityBoardListBinding>(R.layout.activity_board_list) {

    private val viewModel: BoardListViewModel by viewModel()
    private val boardListAdapter by lazy { BoardListAdapter(viewModel) }
    private var uid: String? = null
    private var tag: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        withViewModel()

    }

    override fun onBinding(dataBinding: ActivityBoardListBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvBoardList.adapter = boardListAdapter
    }

    private fun initView() {
        rvBoardList.run {
            addItemDecoration(dividerItemDecoration())
            paging()
        }

        setCloseButtonToolbar(toolbar) {
            finish()
        }

        intent.getParcelableExtra<UserData>(BOARD_DATA_KEY)?.let {
            uid = it.uid
            initData(uid = uid)
            tvTitle.text = getString(R.string.my_board_title_format, it.nickName)
        }

        intent.getStringExtra(TAG_DATA_KEY)?.let {
            tag = it.removeRange(0, 1)
            initData(tag = tag)
            tvTitle.text = it
        }

        swipeLayout.setOnRefreshListener {

            viewModel.run {
                setLoading(LoadingStatus.REFRESH)
                uid?.let {
                    initUserData(it)
                }
                tag?.let {
                    initData(tagQuery = it)
                }
            }
        }
    }

    private fun RecyclerView.paging() {

        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(viewModel) {
                    if (!getLastDate()) {
                        boardList.value?.lastOrNull()?.createDate?.let { date ->
                            uid?.let { moreUserData(date, it) }
                            tag?.let { moreData(date) }
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })

    }

    private fun initData(uid: String? = null, tag: String? = null) {
        viewModel.run {
            setLoading(LoadingStatus.INIT)
            uid?.let {
                initUserData(it)
            }
            tag?.let {
                initData(tagQuery = it)
            }
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            pageToDetailAct()
            completeBoardList()
        }

    }

    private fun BoardListViewModel.completeBoardList() {
        boardList.observe(this@BoardListActivity, Observer {
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
                        data?.getParcelableExtra<BoardData>(BoardDetailActivity.KEY_BOARD_DATA)
                            ?.let {
                                boardListAdapter.updateBoard(it)
                            }
                    }
                    BoardDetailActivity.BOARD_REMOVE_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            boardListAdapter.removeBoard(it)
                        }
                    }
                    BoardDetailActivity.BOARD_NOT_FOUND_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {

                            boardListAdapter.getBoardData(it)?.let { boardData ->
                                viewModel.removeBoard(boardData)
                            }

                            boardListAdapter.removeBoard(it)

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
                                    addBoard(it)
                                }
                            }
                    }
                }
            }
        }
    }

    companion object {
        const val BOARD_LIST_TO_DETAIL_REQ = 1515
        const val BOARD_DATA_KEY = "boardDataKey"
        const val TAG_DATA_KEY = "tagDataKey"
    }
}
