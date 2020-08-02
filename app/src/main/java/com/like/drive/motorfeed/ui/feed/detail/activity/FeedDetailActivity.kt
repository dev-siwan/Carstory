package com.like.drive.motorfeed.ui.feed.detail.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.ActivityFeedDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.UploadActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedDetailActivity : BaseActivity<ActivityFeedDetailBinding>((R.layout.activity_feed_detail)) {

    private var feedData :FeedData?=null
    private val viewModel :FeedDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData(){
        feedData = intent.getParcelableExtra(UploadActivity.CREATE_FEED_DATA_KEY)
        viewModel.initDate(feedData)
    }

}