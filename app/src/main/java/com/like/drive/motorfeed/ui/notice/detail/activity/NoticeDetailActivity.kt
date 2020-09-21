package com.like.drive.motorfeed.ui.notice.detail.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.databinding.ActivityNoticeDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.notice.detail.viewmodel.NoticeDetailViewModel
import kotlinx.android.synthetic.main.activity_notice_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoticeDetailActivity :
    BaseActivity<ActivityNoticeDetailBinding>(R.layout.activity_notice_detail) {

    val viewModel: NoticeDetailViewModel by viewModel()

    private var noticeData: NoticeData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.getParcelableExtra<NoticeData>(NOTICE_DATA_KEY)?.let {
            noticeData = it
            binding?.data = it
            viewModel.getFile(it)
        }

        withViewModel()

    }

    private fun withViewModel() {
        with(viewModel) {
            convertContent()
        }

    }

    private fun NoticeDetailViewModel.convertContent() {
        mdFileContent.observe(this@NoticeDetailActivity, Observer {
            webView.loadFromText(it)
        })
    }

    companion object {
        const val NOTICE_DATA_KEY = "NoticeDataKey"
    }

}