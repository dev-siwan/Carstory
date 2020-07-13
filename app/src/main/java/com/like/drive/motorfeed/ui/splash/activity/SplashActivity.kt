package com.like.drive.motorfeed.ui.splash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.ActivitySplashBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.splash.viewmodel.SplashViewModel
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val viewModel : SplashViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


}