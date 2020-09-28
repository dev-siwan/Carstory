package com.like.drive.motorfeed.ui.notice.list.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.enum.NoticeSelectType
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.databinding.ActivityNoticeListBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.dividerItemDecoration
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startAct
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.dialog.ConfirmDialog
import com.like.drive.motorfeed.ui.notice.detail.activity.NoticeDetailActivity
import com.like.drive.motorfeed.ui.notice.list.adapter.NoticeListAdapter
import com.like.drive.motorfeed.ui.notice.list.fragment.NoticeUploadFragmentDialog
import com.like.drive.motorfeed.ui.notice.list.viewmodel.NoticeListViewModel
import kotlinx.android.synthetic.main.activity_notice_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoticeListActivity : BaseActivity<ActivityNoticeListBinding>(R.layout.activity_notice_list) {

    private val viewModel: NoticeListViewModel by viewModel()
    private val noticeAdapter by lazy { NoticeListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        tvNoticeUpload.setOnClickListener {
            showUploadFragment()
        }

    }

    override fun onBinding(dataBinding: ActivityNoticeListBinding) {
        super.onBinding(dataBinding)

        dataBinding.rvNoticeList.adapter = noticeAdapter
        dataBinding.vm = viewModel

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

            addItemDecoration(dividerItemDecoration())
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
            clickListener()
            menuClick()
            removeComplete()
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

    private fun NoticeListViewModel.removeComplete() {
        removeCompleteEvent.observe(this@NoticeListActivity, Observer {
            noticeAdapter.removeNotice(it.nid)
        })
    }

    private fun NoticeListViewModel.menuClick() {
        clickMenuEvent.observe(this@NoticeListActivity, Observer {
            showListDialog(
                NoticeSelectType.values().map { getString(it.resID) }.toTypedArray()
                , getString(R.string.menu_text)
            ) { position ->
                when (NoticeSelectType.values()[position]) {
                    NoticeSelectType.NOTIFICATION -> {
                        showConfirmDialog(getString(R.string.send_notification_message, it.title)) {
                            viewModel.sendNotification(it)
                        }
                    }
                    NoticeSelectType.DELETE -> {
                        showConfirmDialog(getString(R.string.notice_delete_message, it.title)) {
                            viewModel.removeNotice(it)
                        }
                    }

                    NoticeSelectType.UPDATE -> {
                        showUploadFragment(it)
                    }
                }
            }
        })
    }

    private fun showUploadFragment(noticeData: NoticeData? = null) {
        NoticeUploadFragmentDialog.newInstance(noticeData).apply {
            onComplete = { data, isUpdate ->

                if (isUpdate) noticeAdapter.updateNotice(data) else noticeAdapter.addNotice(data)
                dismiss()

            }
        }.show(supportFragmentManager, "")
    }

    private fun showConfirmDialog(message: String, confirm: () -> Unit) {
        ConfirmDialog.newInstance(message = message).apply {
            confirmAction = {
                confirm.invoke()
                dismiss()
            }
        }.show(supportFragmentManager, ConfirmDialog.TAG)
    }

    private fun NoticeListViewModel.clickListener() {
        clickNoticeDataEvent.observe(this@NoticeListActivity, Observer {
            startAct(NoticeDetailActivity::class, Bundle().apply {
                putParcelable(NoticeDetailActivity.NOTICE_DATA_KEY, it)
            })
        })
    }
}