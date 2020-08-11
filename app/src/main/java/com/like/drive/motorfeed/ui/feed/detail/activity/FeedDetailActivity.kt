package com.like.drive.motorfeed.ui.feed.detail.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.enum.CommentSelectType
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.ActivityFeedDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.hideKeyboard
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.feed.detail.adapter.CommentAdapter
import com.like.drive.motorfeed.ui.feed.detail.adapter.DetailImgAdapter
import com.like.drive.motorfeed.ui.feed.detail.fragment.ReCommentDialogFragment
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import kotlinx.android.synthetic.main.activity_feed_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedDetailActivity :
    BaseActivity<ActivityFeedDetailBinding>((R.layout.activity_feed_detail)) {

    private var feedData: FeedData? = null
    private var fid: String? = null
    private val viewModel: FeedDetailViewModel by viewModel()

    private val commentAdapter by lazy { CommentAdapter(viewModel) }

    private val commentOptions by lazy { resources.getStringArray(R.array.pick_comment_options) }


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
            getParcelableExtra<FeedData>(FeedUploadActivity.CREATE_FEED_DATA_KEY)?.let {
                feedData = it
                viewModel.initDate(it)
            }
            getStringExtra(KEY_FEED_ID)?.let {
                fid = it
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

    private fun withViewModel() {
        with(viewModel) {
            error()
            isProgress()
            addComment()
            removeComment()
            showReCommentDialog()
            showCommentOptions()
        }
    }


    private fun FeedDetailViewModel.isProgress(){
        isProgressEvent.observe(this@FeedDetailActivity, Observer {
            if (it) { if (!loadingProgress.isShowing) loadingProgress.show() } else { loadingProgress.dismiss() }
        })
    }

    private fun FeedDetailViewModel.showReCommentDialog() {
        showReCommentDialogEvent.observe(this@FeedDetailActivity, Observer {
            ReCommentDialogFragment.newInstance(it).show(supportFragmentManager, "")
        })
    }

    private fun FeedDetailViewModel.addComment() {

        //코멘트
        addCommentEvent.observe(this@FeedDetailActivity, Observer {
            comment.value = null
            hideKeyboard(rvComment)
            commentAdapter.run {
                addCommentItem(it)
                rvComment.smoothScrollToPosition(itemCount - 1)
            }
            falseProgress()
        })

        //리코멘트
        addReCommentEvent.observe(this@FeedDetailActivity, Observer { reCommentData ->
            commentAdapter.addReCommentItem(reCommentData)
            falseProgress()
        })
    }

    private fun FeedDetailViewModel.removeComment() {
        //코멘트
        removeCommentEvent.observe(this@FeedDetailActivity, Observer {
            commentAdapter.removeCommentItem(it)
            falseProgress()
        })

        //리코멘트
        removeReCommentEvent.observe(this@FeedDetailActivity, Observer { reCommentData ->
            commentAdapter.removeReCommentItem(reCommentData)
            falseProgress()
        })
    }



    private fun FeedDetailViewModel.showCommentOptions() {

        //코멘트
        optionsCommentEvent.observe(this@FeedDetailActivity, Observer { commentData ->
            showOptionsList(commentData.uid,
                reportCallback = {

                },
                deleteCallback = {
                    removeFeedComment(commentData)
                })
        })

        //리코멘트
        optionsReCommentEvent.observe(this@FeedDetailActivity, Observer { reCommentData ->
            showOptionsList(reCommentData.uid,
                reportCallback = {

                },
                deleteCallback = {
                    removeFeedReComment(reCommentData)
                })
        })
    }

    private fun showOptionsList(
        uid: String?,
        reportCallback: () -> Unit,
        deleteCallback: () -> Unit
    ) {
        val list = uid?.let {
            if (uid == UserInfo.userInfo?.uid ?: "") {
                commentOptions
            } else {
                commentOptions.dropLast(1).toTypedArray()
            }
        } ?: commentOptions.dropLast(1).toTypedArray()

        showListDialog(list, "") {
            when (it) {
                CommentSelectType.REPORT.ordinal -> reportCallback()
                CommentSelectType.DELETE.ordinal -> deleteCallback()
            }
        }
    }

    private fun FeedDetailViewModel.error() {
        errorEvent.observe(this@FeedDetailActivity, Observer {
            showShortToast(it)
        })
        warningSelfLikeEvent.observe(this@FeedDetailActivity, Observer {
            showShortToast(it)
        })
    }

    private fun RecyclerView.setImagePosition() {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val index =
                    (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
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