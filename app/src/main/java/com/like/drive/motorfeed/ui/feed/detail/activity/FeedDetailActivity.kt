package com.like.drive.motorfeed.ui.feed.detail.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.ActivityFeedDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.feed.detail.adapter.CommentAdapter
import com.like.drive.motorfeed.ui.feed.detail.adapter.DetailImgAdapter
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.UploadActivity
import kotlinx.android.synthetic.main.activity_feed_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedDetailActivity :
    BaseActivity<ActivityFeedDetailBinding>((R.layout.activity_feed_detail)) {

    private var feedData: FeedData? = null
    private var fid:String?=null
    private val viewModel: FeedDetailViewModel by viewModel()

    private val commentAdapter by lazy { CommentAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityFeedDetailBinding) {
        super.onBinding(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.rvImg.adapter = DetailImgAdapter()
        dataBinding.rvComment.adapter = commentAdapter

    }

    private fun initData() {
        intent.run {
            getParcelableExtra<FeedData>(UploadActivity.CREATE_FEED_DATA_KEY)?.let {
                feedData = it
                viewModel.initDate(it)
            }
            getStringExtra(KEY_FEED_ID)?.let {
                fid=it
                viewModel.initDate(it)
            }
        }
    }

    private fun initView() {

        rvImg.run {
            setImagePosition()
            setSnapHelper()
        }
    }

    private fun withViewModel(){
        with(viewModel){
            addComment()
            error()
        }
    }

    private fun FeedDetailViewModel.addComment(){
        addCommentEvent.observe(this@FeedDetailActivity, Observer {
            commentAdapter.addComment(it)
        })
    }


    private fun FeedDetailViewModel.error(){
        errorEvent.observe(this@FeedDetailActivity, Observer {
           showShortToast(it)
        })
    }

    private fun RecyclerView.setImagePosition() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val index = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
                viewModel.setPhotoIndex(index)
            }
        })
    }

    private fun RecyclerView.setSnapHelper() {
        onFlingListener = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(this)
    }



    companion object {
        const val KEY_FEED_ID = "FEED_ID"
    }
}