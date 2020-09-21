package com.like.drive.motorfeed.ui.notice.list.activity

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityNoticeListBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.notice.list.adapter.NoticeListAdapter
import com.like.drive.motorfeed.ui.notice.list.fragment.NoticeUploadFragmentDialog
import com.like.drive.motorfeed.ui.notice.list.viewmodel.NoticeListViewModel
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoticeListActivity : BaseActivity<ActivityNoticeListBinding>(R.layout.activity_notice_list) {

    private val viewModel: NoticeListViewModel by viewModel()
    private val noticeAdapter by lazy { NoticeListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        tvNoticeUpload.setOnClickListener {
            NoticeUploadFragmentDialog.newInstance().apply {
                onComplete = {
                    noticeAdapter.addFeed(it)
                    dismiss()
                }
            }.show(supportFragmentManager, "")
        }

    }

    override fun onBinding(dataBinding: ActivityNoticeListBinding) {
        super.onBinding(dataBinding)

        dataBinding.rvNoticeList.adapter = noticeAdapter

    }

    private fun initView() {
        rvNoticeList.apply {

            withPaging(object : PagingCallback {
                override fun requestMoreList() {

                    with(viewModel) {
                        if (!getLastDate()) {
                            noticeList.value?.lastOrNull()?.createDate?.let {
                                moreData(date = it)
                            }
                        }
                    }
                }

                override fun isRequest(): Boolean = false

            })

            val dividerItemDecoration =
                DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL).apply {
                    ContextCompat.getDrawable(this@NoticeListActivity, R.drawable.divider_feed_list)
                        ?.let {
                            setDrawable(it)
                        }
                }

            addItemDecoration(dividerItemDecoration)
        }


        initData()
        withViewModel()
    }

    private fun initData() {

        if (noticeAdapter.noticeList.isEmpty()) {
            viewModel.initData()
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            getNoticeList()
        }
    }

    private fun NoticeListViewModel.getNoticeList() {
        noticeList.observe(this@NoticeListActivity, Observer {
            noticeAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
    }
}