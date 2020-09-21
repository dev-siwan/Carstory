package com.like.drive.motorfeed.ui.feed.list.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.databinding.ActivityFeedListBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import kotlinx.android.synthetic.main.activity_feed_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedListActivity : BaseActivity<ActivityFeedListBinding>(R.layout.activity_feed_list) {

    private val viewModel: FeedListViewModel by viewModel()
    private val feedListAdapter by lazy { FeedListAdapter(viewModel) }
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        withViewModel()

    }

    override fun onBinding(dataBinding: ActivityFeedListBinding) {
        super.onBinding(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = feedListAdapter
    }

    private fun initView() {
        val decorationItem = DividerItemDecoration(
            this, DividerItemDecoration.VERTICAL
        ).apply {
            ContextCompat.getDrawable(
                this@FeedListActivity, R.drawable.line_solid_grey_6
            )?.let { setDrawable(it) }
        }
        rvFeedList?.run {
            addItemDecoration(decorationItem)
            paging()
        }

        setCloseButtonToolbar(toolbar) {
            finish()

        }

        intent.getParcelableExtra<UserData>(FEED_DATE_KEY)?.let {
            uid = it.uid
            viewModel.loadingStatus = LoadingStatus.INIT
            initData(uid)
            tvTitle.text = getString(R.string.my_feed_title_format, it.nickName)
        }

        swipeLayout.setOnRefreshListener {
            uid?.let {
                viewModel.loadingStatus = LoadingStatus.REFRESH
                viewModel.initUserData(it)
            }
        }

    }

    private fun RecyclerView.paging() {

        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(viewModel) {
                    if (!getLastDate()) {
                        feedList.value?.lastOrNull()?.createDate?.let { date ->
                            uid?.let { moreUserData(date, it) }
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })

    }

    private fun initData(uid: String?) {
        uid?.let { viewModel.initUserData(it) }
    }

    private fun withViewModel() {
        with(viewModel) {
            pageToDetailAct()
            completeFeedList()
        }

    }

    private fun FeedListViewModel.completeFeedList() {
        feedList.observe(this@FeedListActivity, Observer {
            feedListAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
    }

    private fun FeedListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(this@FeedListActivity, Observer {
            startActForResult(FeedDetailActivity::class, FEED_LIST_TO_DETAIL_REQ, Bundle().apply {
                putString(FeedDetailActivity.KEY_FEED_ID, it)
            })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FEED_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    FeedDetailActivity.FEED_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedListAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getStringExtra(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedListAdapter.removeFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_NOT_FOUND_RES_CODE -> {
                        data?.getStringExtra(FeedDetailActivity.KEY_FEED_DATA)?.let {

                            feedListAdapter.getFeedData(it)?.let { feedData ->
                                viewModel.removeFeed(feedData)
                            }

                            feedListAdapter.removeFeed(it)

                        }
                    }
                }
            }

            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<FeedData>(FeedUploadActivity.FEED_CREATE_KEY)
                            ?.let {
                                feedListAdapter.run {
                                    addFeed(it)
                                }
                            }
                    }
                }
            }
        }
    }

    companion object {
        const val FEED_LIST_TO_DETAIL_REQ = 1515
        const val FEED_DATE_KEY = "feedDataKey"
    }
}
