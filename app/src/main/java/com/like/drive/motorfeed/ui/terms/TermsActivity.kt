package com.like.drive.motorfeed.ui.terms

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.os.Bundle
import android.webkit.*
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityTermsBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : BaseActivity<ActivityTermsBinding>(R.layout.activity_terms) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webViewSetting()
        init()
    }

    private fun init() {
        intent.getStringExtra(TERMS_KEY)?.let {
            when (it) {
                TERMS_USE_VALUE -> {
                    tvWelcome.text = getString(R.string.more_title_push_terms_use)
                }
                else -> {
                    tvWelcome.text = getString(R.string.more_title_push_terms_privacy)
                    load(TERMS_PRIVACY_URL)
                }
            }
        }
    }

    private fun load(url: String) {
        webView.loadUrl(url)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetting() {

        with(webView) {
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setAppCacheEnabled(false)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webChromeClient = WebChromeClient()
            webViewClient = MyWebViewClient()
        }

    }

    private class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request != null) {
                val url = request.url.toString()
                view?.loadUrl(url)
            }
            return true
        }
    }

    companion object {
        const val TERMS_KEY = "termsKey"
        const val TERMS_USE_VALUE = "termsUseValue"
        const val TERMS_PRIVACY_VALUE = "termsPrivacyValue"
        const val TERMS_PRIVACY_URL =
            "https://docs.google.com/document/d/1Kl_CXSFLL0Nq3gCxq_qZUTLyeIeBVptnLmqK7YFYhiQ/edit?usp=sharing"

    }
}