package com.like.drive.carstory.ui.notice.detail.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.like.drive.carstory.R
import com.like.drive.carstory.common.define.GitDefine
import com.like.drive.carstory.common.interceptor.GitHubInterceptor
import com.like.drive.carstory.data.notice.NoticeData
import com.like.drive.carstory.databinding.ActivityNoticeDetailBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.notice.detail.viewmodel.NoticeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.markdownview.Config
import es.dmoral.markdownview.MarkdownView
import kotlinx.android.synthetic.main.activity_notice_detail.*
import okhttp3.OkHttpClient

@AndroidEntryPoint
class NoticeDetailActivity :
    BaseActivity<ActivityNoticeDetailBinding>(R.layout.activity_notice_detail) {

    val viewModel: NoticeDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
        withViewModel()

    }

    private fun initData() {
        intent.getParcelableExtra<NoticeData>(NOTICE_DATA_KEY)?.let {
            binding?.data = it
            setMarkDown(it)
        }

        intent.getStringExtra(NOTICE_ID_KEY)?.let {
            viewModel.getNotice(it)
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            completeNotice()
            error()
        }
    }

    private fun NoticeDetailViewModel.completeNotice() {
        noticeData.observe(this@NoticeDetailActivity, Observer {
            binding?.data = it
            setMarkDown(it)
        })
    }

    private fun NoticeDetailViewModel.error() {
        errorEvent.observe(this@NoticeDetailActivity, Observer {
            showShortToast(it)
        })
    }

    private fun setMarkDown(data: NoticeData) {
        webView.run {
            setCurrentConfig(Config.getDefaultConfig().apply {
                defaultOkHttpClient =
                    OkHttpClient().newBuilder().addInterceptor(GitHubInterceptor()).build()
            })

            data.mdFile?.also { file -> loadFromUrl(GitDefine.getNoticeFile(file)) }

            setOnMarkdownRenderingListener(object : MarkdownView.OnMarkdownRenderingListener {
                override fun onMarkdownRenderError() {
                    progressBar.isVisible = false
                }

                override fun onMarkdownFinishedRendering() {
                    progressBar.isVisible = false
                }

            })
        }
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) {
            finish()
        }
    }

    companion object {
        const val NOTICE_DATA_KEY = "NoticeDataKey"
        const val NOTICE_ID_KEY = "NoticeId"
    }

}