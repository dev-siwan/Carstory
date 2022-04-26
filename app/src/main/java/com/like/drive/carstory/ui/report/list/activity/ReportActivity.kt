package com.like.drive.carstory.ui.report.list.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.ActivityReportBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.etc.PagingCallback
import com.like.drive.carstory.ui.base.ext.dividerItemDecoration
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.base.ext.startAct
import com.like.drive.carstory.ui.base.ext.withPaging
import com.like.drive.carstory.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.carstory.ui.dialog.ConfirmDialog
import com.like.drive.carstory.ui.report.list.adapter.ReportAdapter
import com.like.drive.carstory.ui.report.list.viewmodel.ReportViewModel
import com.like.drive.carstory.ui.user.activity.UserLookUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_report.*

@AndroidEntryPoint
class ReportActivity : BaseActivity<ActivityReportBinding>(R.layout.activity_report) {

    private val viewModel: ReportViewModel by viewModels()
    private val reportAdapter by lazy { ReportAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityReportBinding) {
        dataBinding.rvReportList.run {
            paging()
            addItemDecoration(dividerItemDecoration())
            adapter = reportAdapter
        }
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
        initData()
    }

    private fun withViewModel() {
        with(viewModel) {
            loadList()
            removeListener()
            removeComplete()
            errorMessage()
            moveDetailBoard()
            moveUserLookUp()
        }
    }

    private fun ReportViewModel.removeListener() {
        removeListenerEvent.observe(this@ReportActivity, Observer {
            ConfirmDialog.newInstance(message = getString(R.string.report_remove_listener_message))
                .apply {
                    confirmAction = { viewModel.removeReport(it) }
                }.show(supportFragmentManager, ConfirmDialog.TAG)
        })
    }

    private fun ReportViewModel.removeComplete() {
        removeCompleteEvent.observe(this@ReportActivity, Observer {
            reportAdapter.removeReport(it.rid)
        })
    }

    private fun ReportViewModel.moveDetailBoard() {
        detailBoardEvent.observe(this@ReportActivity, Observer {
            startAct(BoardDetailActivity::class, Bundle().apply {
                putString(BoardDetailActivity.KEY_BOARD_ID, it)
            })
        })
    }

    private fun ReportViewModel.moveUserLookUp() {
        userLookUpEvent.observe(this@ReportActivity, Observer {
            startAct(UserLookUpActivity::class, Bundle().apply {
                putString(UserLookUpActivity.USER_ID_KEY, it.uid)
            })
        })
    }

    private fun ReportViewModel.errorMessage() {
        errorEvent.observe(this@ReportActivity, Observer {
            showShortToast(it)
        })
    }

    private fun initData() {

        if (reportAdapter.itemCount == 0) {
            viewModel.initData()
        }
    }

    private fun ReportViewModel.loadList() {
        reportList.observe(this@ReportActivity, Observer {
            reportAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
    }

    private fun RecyclerView.paging() {
        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(viewModel) {
                    if (!getLastDate()) {
                        reportList.value?.lastOrNull()?.createDate?.let {
                            moreData(date = it)
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })
    }
}