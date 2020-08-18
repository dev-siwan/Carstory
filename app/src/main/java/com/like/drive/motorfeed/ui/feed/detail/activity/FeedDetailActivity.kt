package com.like.drive.motorfeed.ui.feed.detail.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.enum.OptionsSelectType
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.databinding.ActivityFeedDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.*
import com.like.drive.motorfeed.ui.feed.detail.adapter.CommentAdapter
import com.like.drive.motorfeed.ui.feed.detail.adapter.DetailImgAdapter
import com.like.drive.motorfeed.ui.feed.detail.fragment.CommentDialogFragment
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.view.large.activity.LargeThanActivity
import kotlinx.android.synthetic.main.activity_feed_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedDetailActivity :
    BaseActivity<ActivityFeedDetailBinding>((R.layout.activity_feed_detail)) {

    private var feedData: FeedData? = null
    private var fid: String? = null
    private val viewModel: FeedDetailViewModel by viewModel()

    private val commentAdapter by lazy { CommentAdapter(viewModel) }
    private val detailImgAdapter by lazy { DetailImgAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        withViewModel()
    }

    override fun onBinding(dataBinding: ActivityFeedDetailBinding) {
        super.onBinding(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.rvImg.adapter = detailImgAdapter
        dataBinding.rvComment.adapter = commentAdapter

    }

    private fun initData() {
        intent.run {
            getParcelableExtra<FeedData>(FeedUploadActivity.FEED_CREATE_KEY)?.let {
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

        setBackButtonToolbar(toolbar){onBackPressed()}
    }

    private fun withViewModel() {
        with(viewModel) {
            error()
            imgClick()
            isProgress()
            removeFeed()
            addComment()
            removeComment()
            showCommentFragmentDialog()
            showOptions()
            updateComment()
        }
    }


    private fun FeedDetailViewModel.isProgress(){
        isProgressEvent.observe(this@FeedDetailActivity, Observer {
            if (it) { if (!loadingProgress.isShowing) loadingProgress.show() } else { loadingProgress.dismiss() }
        })
    }

    private fun FeedDetailViewModel.showCommentFragmentDialog() {
        showCommentDialogEvent.observe(this@FeedDetailActivity, Observer {
            CommentDialogFragment.newInstance(it).show(supportFragmentManager, "")
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

    private fun FeedDetailViewModel.updateComment(){
        updateCommentEvent.observe(this@FeedDetailActivity, Observer {
            commentAdapter.updateCommentItem(it)
            falseProgress()
        })

        updateReCommentEvent.observe(this@FeedDetailActivity, Observer {
            commentAdapter.updateReCommentItem(it)
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



    private fun FeedDetailViewModel.showOptions() {

        //게시물
        optionFeedEvent.observe(this@FeedDetailActivity, Observer { feedData->
            showOptionsList(feedData.uid,
            reportCallback = {},
            deleteCallback = { viewModel.removeFeedListener() },
            updateCallback = {
                startActForResult(FeedUploadActivity::class,FEED_UPLOAD_REQ_CODE,Bundle().apply { putParcelable(FeedUploadActivity.FEED_UPDATE_KEY,feedData) })
            })
        })
        //코멘트
        optionsCommentEvent.observe(this@FeedDetailActivity, Observer { commentData ->
            showOptionsList(commentData.uid,
                reportCallback = {

                },
                deleteCallback = {
                    removeFeedComment(commentData)
                },
                updateCallback={
                    showCommentDialogListener(true,commentData,null)
                })
        })

        //리코멘트
        optionsReCommentEvent.observe(this@FeedDetailActivity, Observer { reCommentData ->
            showOptionsList(reCommentData.uid,
                reportCallback = {

                },
                deleteCallback = {
                    removeFeedReComment(reCommentData)
                },
                updateCallback={
                    showCommentDialogListener(true,null,reCommentData)
                })
        })
    }

    private fun FeedDetailViewModel.removeFeed(){
        removeFeedEvent.observe(this@FeedDetailActivity, Observer {
            setResult(FEED_REMOVE_RES_CODE,Intent().apply {
                putExtra(KEY_FEED_DATA,it)
            })
            finish()
        })
    }

    /**
     *  이미지 클릭 이벤트
     */
    private fun FeedDetailViewModel.imgClick(){
        imgUrlClickEvent.observe(this@FeedDetailActivity, Observer {
            startAct(LargeThanActivity::class, Bundle().apply {

                val list = detailImgAdapter.currentList.map { PhotoData(null,it) } as ArrayList

                putParcelableArrayList(
                    LargeThanActivity.KEY_PHOTO_DATA_ARRAY,
                    list
                )
                putInt(
                    LargeThanActivity.KEY_PHOTO_DATA_ARRAY_POSITION,
                    detailImgAdapter.currentList.indexOf(it)
                )
            })
        })
    }

    /** 신고하기,수정,삭제
     * 자신의 아이디가 아니면 수정,삭제 표시
     * 아니면 신고하기 표시**/
    private fun showOptionsList(
        uid: String?,
        reportCallback: () -> Unit,
        deleteCallback: () -> Unit,
        updateCallback: () -> Unit
    ) {
        val list = uid?.let {
            if (uid == UserInfo.userInfo?.uid ?: "") {
                OptionsSelectType.values().drop(1).map { getString(it.resID) }.toTypedArray()
            } else {
                OptionsSelectType.values().dropLast(2).map { getString(it.resID) }.toTypedArray()
            }
        } ?: OptionsSelectType.values().dropLast(2).map { getString(it.resID) }.toTypedArray()

        showListDialog(list, "") {
            when (list[it]) {
                getString(OptionsSelectType.REPORT.resID) -> reportCallback()
                getString(OptionsSelectType.DELETE.resID) -> deleteCallback()
                getString(OptionsSelectType.UPDATE.resID) -> updateCallback()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FEED_UPLOAD_REQ_CODE -> {
                    data?.getParcelableExtra<FeedData>(FeedUploadActivity.FEED_UPDATE_KEY)?.let {
                        viewModel.initDate(it)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        viewModel.feedData.value?.let {
            setResult(FEED_UPLOAD_RES_CODE,Intent().apply {
                putExtra(KEY_FEED_DATA,it)
            })
            finish()
        }?:super.onBackPressed()
    }

    companion object {
        const val KEY_FEED_ID = "FEED_ID"
        const val KEY_FEED_DATA= "FEED_DATA"
        const val FEED_UPLOAD_REQ_CODE=1055
        const val FEED_UPLOAD_RES_CODE=1056
        const val FEED_REMOVE_RES_CODE=1057
    }
}