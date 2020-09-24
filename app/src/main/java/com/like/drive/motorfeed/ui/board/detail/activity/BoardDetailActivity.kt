package com.like.drive.motorfeed.ui.board.detail.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.DisplayMetrics
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.enum.OptionsSelectType
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.databinding.ActivityBoardDetailBinding
import com.like.drive.motorfeed.ui.base.BaseActivity
import com.like.drive.motorfeed.ui.base.ext.*
import com.like.drive.motorfeed.ui.board.detail.adapter.CommentAdapter
import com.like.drive.motorfeed.ui.board.detail.adapter.DetailImgAdapter
import com.like.drive.motorfeed.ui.board.detail.fragment.CommentDialogFragment
import com.like.drive.motorfeed.ui.board.detail.viewmodel.BoardDetailViewModel
import com.like.drive.motorfeed.ui.board.upload.activity.UploadActivity
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.view.large.activity.LargeThanActivity
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.layout_detail_function_block.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardDetailActivity :
    BaseActivity<ActivityBoardDetailBinding>((R.layout.activity_board_detail)) {

    private var boardData: BoardData? = null
    private var fid: String? = null
    private val viewModel: BoardDetailViewModel by viewModel()

    private val commentAdapter by lazy { CommentAdapter(viewModel) }
    private val detailImgAdapter by lazy { DetailImgAdapter(viewModel) }

    @Suppress("DEPRECATION")
    private val adSize: AdSize
        get() {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                display
            } else {
                windowManager.defaultDisplay
            }
            val outMetrics = DisplayMetrics()
            display?.getRealMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = containerBanner.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getPortraitAnchoredAdaptiveBannerAdSize(this, adWidth)
        }
    private var initialLayoutComplete = false
    private var adView: AdView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        withViewModel()
        advInit()
    }

    override fun onBinding(dataBinding: ActivityBoardDetailBinding) {
        super.onBinding(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.rvImg.adapter = detailImgAdapter
        dataBinding.rvComment.adapter = commentAdapter
        dataBinding.tvContent.apply {
            movementMethod = LinkMovementMethod.getInstance()
        }

    }

    private fun initData() {
        intent.run {
            getParcelableExtra<BoardData>(UploadActivity.BOARD_CREATE_KEY)?.let {
                boardData = it
                viewModel.initDate(it)
            }
            getStringExtra(KEY_BOARD_ID)?.let {
                fid = it
                viewModel.run {
                    loadingStatus = LoadingStatus.INIT
                    initDate(it)
                }
            }
        }
    }

    private fun initView() {
        rvImg.run {
            setImagePosition()
            setSnapHelper()
        }

        setBackButtonToolbar(toolbar) { onBackPressed() }

        containerDetailFunction.containerShare.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, viewModel.boardData.value?.title)
            }.run {
                val sharing = Intent.createChooser(this, "공유하기")
                startActivity(sharing)
            }
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.run {
                loadingStatus = LoadingStatus.REFRESH
                boardData.value?.fid?.let {
                    initDate(it)
                } ?: isRefresh.set(false)
            }
        }
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
            finishView()
        }
    }

    private fun BoardDetailViewModel.isProgress() {
        isProgressEvent.observe(this@BoardDetailActivity, Observer {
            if (it) {
                if (!loadingProgress.isShowing) loadingProgress.show()
            } else {
                loadingProgress.dismiss()
            }
        })
    }

    private fun BoardDetailViewModel.showCommentFragmentDialog() {
        showCommentDialogEvent.observe(this@BoardDetailActivity, Observer {
            CommentDialogFragment.newInstance(it).show(supportFragmentManager, "")
        })
    }

    private fun BoardDetailViewModel.addComment() {

        //코멘트
        addCommentEvent.observe(this@BoardDetailActivity, Observer {
            comment.value = null
            hideKeyboard(rvComment)
            commentAdapter.run {
                addCommentItem(it)
                rvComment.smoothScrollToPosition(itemCount - 1)
            }
            falseProgress()
        })

        //리코멘트
        addReCommentEvent.observe(this@BoardDetailActivity, Observer { reCommentData ->
            commentAdapter.addReCommentItem(reCommentData)
            falseProgress()
        })
    }

    private fun BoardDetailViewModel.updateComment() {
        updateCommentEvent.observe(this@BoardDetailActivity, Observer {
            commentAdapter.updateCommentItem(it)
            falseProgress()
        })

        updateReCommentEvent.observe(this@BoardDetailActivity, Observer {
            commentAdapter.updateReCommentItem(it)
            falseProgress()
        })
    }

    private fun BoardDetailViewModel.removeComment() {
        //코멘트
        removeCommentEvent.observe(this@BoardDetailActivity, Observer {
            commentAdapter.removeCommentItem(it)
            falseProgress()
        })

        //리코멘트
        removeReCommentEvent.observe(this@BoardDetailActivity, Observer { reCommentData ->
            commentAdapter.removeReCommentItem(reCommentData)
            falseProgress()
        })
    }

    private fun BoardDetailViewModel.showOptions() {

        //게시물
        optionFeedEvent.observe(this@BoardDetailActivity, Observer { feedData ->
            showOptionsList(feedData.userInfo?.uid,
                reportCallback = {},
                deleteCallback = { viewModel.removeFeedListener() },
                updateCallback = {
                    startActForResult(
                        UploadActivity::class,
                        BOARD_UPLOAD_REQ_CODE,
                        Bundle().apply {
                            putParcelable(
                                UploadActivity.BOARD_UPDATE_KEY,
                                feedData
                            )
                        })
                })
        })
        //코멘트
        optionsCommentEvent.observe(this@BoardDetailActivity, Observer { commentData ->
            showOptionsList(commentData.userInfo?.uid,
                reportCallback = {

                },
                deleteCallback = {
                    removeFeedComment(commentData)
                },
                updateCallback = {
                    showCommentDialogListener(true, commentData, null)
                })
        })

        //리코멘트
        optionsReCommentEvent.observe(this@BoardDetailActivity, Observer { reCommentData ->
            showOptionsList(reCommentData.userInfo?.uid,
                reportCallback = {

                },
                deleteCallback = {
                    removeFeedReComment(reCommentData)
                },
                updateCallback = {
                    showCommentDialogListener(true, null, reCommentData)
                })
        })
    }

    private fun BoardDetailViewModel.removeFeed() {
        removeFeedEvent.observe(this@BoardDetailActivity, Observer {
            setResult(BOARD_REMOVE_RES_CODE, Intent().apply {
                putExtra(KEY_BOARD_DATA, it.fid)
            })
            finish()
        })
    }

    private fun BoardDetailViewModel.finishView() {
        finishEvent.observe(this@BoardDetailActivity, Observer {
            setResult(BOARD_NOT_FOUND_RES_CODE, Intent().apply {
                putExtra(KEY_BOARD_DATA, it)
            })
            finish()
        })
    }

    /**
     *  이미지 클릭 이벤트
     */
    private fun BoardDetailViewModel.imgClick() {
        imgUrlClickEvent.observe(this@BoardDetailActivity, Observer {
            startAct(LargeThanActivity::class, Bundle().apply {

                val list = detailImgAdapter.currentList.map { PhotoData(null, it) } as ArrayList

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

    private fun BoardDetailViewModel.error() {
        errorEvent.observe(this@BoardDetailActivity, Observer {
            showShortToast(it)
        })
        warningSelfLikeEvent.observe(this@BoardDetailActivity, Observer {
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
                BOARD_UPLOAD_REQ_CODE -> {
                    data?.getParcelableExtra<BoardData>(UploadActivity.BOARD_UPDATE_KEY)?.let {
                        viewModel.initDate(it)
                    }
                }
            }
        }
    }

    override fun onResume() {
        adView?.resume()
        super.onResume()
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        adView?.destroy()
        super.onDestroy()
    }

    private fun advInit() {

        initialLayoutComplete = false

        MobileAds.initialize(this)

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )

        adView = AdView(this)

        containerBanner.apply {
            addView(adView)
            viewTreeObserver.addOnGlobalLayoutListener {
                if (!initialLayoutComplete) {
                    initialLayoutComplete = true
                    loadBanner()
                }
            }
        }

    }

    private fun loadBanner() {
        adView?.adUnitId = "ca-app-pub-3940256099942544/9214589741"

        adView?.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView?.loadAd(adRequest)

        adView?.run {
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    containerBanner.isVisible = true
                }
            }
        }

    }

    override fun onBackPressed() {
        viewModel.boardData.value?.let {
            setResult(BOARD_UPLOAD_RES_CODE, Intent().apply {
                putExtra(KEY_BOARD_DATA, it)
            })
            finish()
        } ?: super.onBackPressed()
    }

    companion object {
        const val KEY_BOARD_ID = "FEED_ID"
        const val KEY_BOARD_DATA = "FEED_DATA"
        const val BOARD_UPLOAD_REQ_CODE = 1055
        const val BOARD_UPLOAD_RES_CODE = 1056
        const val BOARD_REMOVE_RES_CODE = 1057
        const val BOARD_NOT_FOUND_RES_CODE = 1058
    }
}