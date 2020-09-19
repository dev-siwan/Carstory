package com.like.drive.motorfeed.ui.terms

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivityTermsBinding
import com.like.drive.motorfeed.remote.api.terms.TermsApi
import com.like.drive.motorfeed.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_terms.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import retrofit2.Retrofit

class TermsActivity : BaseActivity<ActivityTermsBinding>(R.layout.activity_terms) {

    private val retrofit: Retrofit by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        initView()
    }

    private fun initView() {
        setCloseButtonToolbar(toolbar)
    }

    private fun init() {

        progressBar.isVisible = true

        intent.getStringExtra(TERMS_KEY)?.let {
            when (it) {
                TERMS_USE_VALUE -> {
                    tvWelcome.text = getString(R.string.more_title_push_terms_use)
                    lifecycleScope.launch {
                        retrofit.create(TermsApi::class.java).getTermsUser().body()?.let { body ->
                            load(body)
                        }
                    }
                }

                else -> {
                    tvWelcome.text = getString(R.string.more_title_push_terms_privacy)
                    lifecycleScope.launch {
                        retrofit.create(TermsApi::class.java).getTermsPrivacy().body()
                            ?.let { body ->
                                load(body)
                            }
                    }
                }
            }
        }
    }

    private fun load(body: String) {

        webView.loadFromText(body)
    }

    companion object {
        const val TERMS_KEY = "termsKey"
        const val TERMS_USE_VALUE = "termsUseValue"
        const val TERMS_PRIVACY_VALUE = "termsPrivacyValue"
    }
}