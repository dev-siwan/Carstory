package com.like.drive.motorfeed.ui.notice.detail.activity

import android.os.Bundle
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.GitDefine
import com.like.drive.motorfeed.common.interceptor.GitHubInterceptor
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.databinding.ActivityNoticeDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.notice.detail.viewmodel.NoticeDetailViewModel
import es.dmoral.markdownview.Config
import kotlinx.android.synthetic.main.activity_notice_detail.*
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoticeDetailActivity :
    BaseActivity<ActivityNoticeDetailBinding>(R.layout.activity_notice_detail) {

    val viewModel: NoticeDetailViewModel by viewModel()

    private var noticeData: NoticeData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

        intent.getParcelableExtra<NoticeData>(NOTICE_DATA_KEY)?.let {
            noticeData = it
            binding?.data = it

            webView.run {
                setCurrentConfig(Config.getDefaultConfig().apply {
                    defaultOkHttpClient =
                        OkHttpClient().newBuilder().addInterceptor(GitHubInterceptor()).build()
                })

                it.mdFile?.also { file -> loadFromUrl(GitDefine.getNoticeFile(file)) }

            }
        }

    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) {
            finish()
        }
    }

    companion object {
        const val NOTICE_DATA_KEY = "NoticeDataKey"
    }

}