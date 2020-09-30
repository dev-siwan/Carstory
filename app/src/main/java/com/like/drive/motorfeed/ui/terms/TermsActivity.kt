package com.like.drive.motorfeed.ui.terms

import android.os.Bundle
import androidx.core.view.isVisible
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.define.GitDefine
import com.like.drive.motorfeed.common.interceptor.GitHubInterceptor
import com.like.drive.motorfeed.databinding.ActivityTermsBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import es.dmoral.markdownview.Config
import es.dmoral.markdownview.MarkdownView
import kotlinx.android.synthetic.main.activity_terms.*
import okhttp3.OkHttpClient

class TermsActivity : BaseActivity<ActivityTermsBinding>(R.layout.activity_terms) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initView()
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar) { finish() }
    }

    private fun init() {

        progressBar.isVisible = true

        intent.getStringExtra(TERMS_KEY)?.let {
            when (it) {
                TERMS_USE_VALUE -> {
                    tvWelcome.text = getString(R.string.more_title_terms_use)
                    load(GitDefine.TERMS_USE_URL)
                }

                else -> {
                    tvWelcome.text = getString(R.string.more_title_terms_privacy)
                    load(GitDefine.PRIVACY_URL)
                }
            }
        }
    }

    private fun load(url: String) {
        webView.run {
            setCurrentConfig(Config.getDefaultConfig().apply {
                defaultOkHttpClient =
                    OkHttpClient().newBuilder().addInterceptor(GitHubInterceptor()).build()
            })

            loadFromUrl(url)

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

    companion object {
        const val TERMS_KEY = "termsKey"
        const val TERMS_USE_VALUE = "termsUseValue"
        const val TERMS_PRIVACY_VALUE = "termsPrivacyValue"
    }
}