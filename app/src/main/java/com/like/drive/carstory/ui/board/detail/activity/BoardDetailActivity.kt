package com.like.drive.carstory.ui.board.detail.activity

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
import com.like.drive.carstory.BuildConfig
import com.like.drive.carstory.R
import com.like.drive.carstory.common.enum.OptionsSelectType
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.photo.PhotoData
import com.like.drive.carstory.data.user.UserData
import com.like.drive.carstory.databinding.ActivityBoardDetailBinding
import com.like.drive.carstory.ui.base.BaseActivity
import com.like.drive.carstory.ui.base.ext.*
import com.like.drive.carstory.ui.board.detail.adapter.CommentAdapter
import com.like.drive.carstory.ui.board.detail.adapter.DetailImgAdapter
import com.like.drive.carstory.ui.board.detail.fragment.CommentDialogFragment
import com.like.drive.carstory.ui.board.detail.viewmodel.BoardDetailViewModel
import com.like.drive.carstory.ui.board.upload.activity.UploadActivity
import com.like.drive.carstory.ui.common.data.LoadingStatus
import com.like.drive.carstory.ui.dialog.ConfirmDialog
import com.like.drive.carstory.ui.report.reg.fragment.ReportRegisterFragmentDialog
import com.like.drive.carstory.ui.user.activity.UserLookUpActivity
import com.like.drive.carstory.ui.view.large.activity.LargeThanActivity
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.layout_detail_function_block.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class BoardDetailActivity :
    BaseActivity<ActivityBoardDetailBinding>((R.layout.activity_board_detail)) {

    private var boardData: BoardData? = null
    private var bid: String? = null
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
        dataBinding.containerDetailFunction.tvComment.setOnClickListener {
            dataBinding.etComment.requestFocus()
            showKeyboard(dataBinding.etComment)
            dataBinding.nestedScrollView.scrollY = dataBinding.nestedScrollView.bottom
        }

    }

    private fun initData() {
        intent.run {
            getParcelableExtra<BoardData>(UploadActivity.BOARD_CREATE_KEY)?.let {
                boardData = it
                viewModel.initDate(it)
            }
            getStringExtra(KEY_BOARD_ID)?.let {
                bid = it
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

            viewModel.makeShareUrlLink()
        }

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.run {
                loadingStatus = LoadingStatus.REFRESH
                boardData.value?.bid?.let {
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
            removeBoard()
            addComment()
            removeComment()
            showCommentFragmentDialog()
            showOptions()
            updateComment()
            finishView()
            completeReport()
            moveUserLookUpPage()
            shareLink()
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
        optionEvent.observe(this@BoardDetailActivity, Observer { boardData ->
            showOptionsList(boardData.userInfo?.uid,
                reportCallback = {
                    showReportFragment(
                        boardData.bid!!,
                        boardData.title!!,
                        boardData.userInfo!!
                    )
                },
                deleteCallback = {
                    showConfirmDialog(getString(R.string.remove_board_confirm_message)) {
                        viewModel.removeBoardListener()
                    }
                },
                updateCallback = {
                    startActForResult(
                        UploadActivity::class,
                        BOARD_UPLOAD_REQ_CODE,
                        Bundle().apply {
                            putParcelable(
                                UploadActivity.BOARD_UPDATE_KEY,
                                boardData
                            )
                        })
                },
                profileCallback = {
                    boardData.userInfo?.uid?.let {
                        moveToUserLookUpPage(it)
                    }
                })
        })
        //코멘트
        optionsCommentEvent.observe(this@BoardDetailActivity, Observer { commentData ->
            showOptionsList(commentData.userInfo?.uid,
                reportCallback = {
                    showReportFragment(
                        commentData.bid!!,
                        commentData.commentStr!!,
                        commentData.userInfo!!
                    )
                },
                deleteCallback = {
                    showConfirmDialog(getString(R.string.remove_comment_confirm_message)) {
                        removeBoardComment(commentData)
                    }
                },
                updateCallback = {
                    showCommentDialogListener(true, commentData, null)
                },
                profileCallback = {
                    commentData.userInfo?.uid?.let {
                        moveToUserLookUpPage(it)
                    }
                })
        })

        //리코멘트
        optionsReCommentEvent.observe(this@BoardDetailActivity, Observer { reCommentData ->
            showOptionsList(reCommentData.userInfo?.uid,
                reportCallback = {
                    showReportFragment(
                        reCommentData.bid!!,
                        reCommentData.commentStr!!,
                        reCommentData.userInfo!!
                    )
                },
                deleteCallback = {
                    showConfirmDialog(getString(R.string.remove_comment_confirm_message)) {
                        removeBoardReComment(reCommentData)
                    }

                },
                updateCallback = {
                    showCommentDialogListener(true, null, reCommentData)
                },
                profileCallback = {
                    reCommentData.userInfo?.uid?.let {
                        moveToUserLookUpPage(it)
                    }
                })
        })
    }

    private fun BoardDetailViewModel.removeBoard() {
        removeBoardEvent.observe(this@BoardDetailActivity, Observer {
            setResult(BOARD_REMOVE_RES_CODE, Intent().apply {
                putExtra(KEY_BOARD_DATA, it.bid)
            })
            finish()
        })
    }

    private fun BoardDetailViewModel.moveUserLookUpPage() {
        moveUserLookUpEvent.observe(this@BoardDetailActivity, Observer {
            moveToUserLookUpPage(it)
        })
    }

    private fun BoardDetailViewModel.shareLink() {
        shareLinkCompleteEvent.observe(this@BoardDetailActivity, Observer {

            val message = getString(
                R.string.share_message,
                viewModel.boardData.value?.title ?: "",
                it.toString()
            )

            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, message)
            }.run {
                val sharing = Intent.createChooser(this, getString(R.string.do_share))
                startActivity(sharing)
            }
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

    /** 신고하기,수정,삭제,프로필보기
     * 자신의 아이디 또는 관리자이면 수정,삭제,프로필보기 표시
     * 아니면 신고하기 표시**/
    private fun showOptionsList(
        uid: String?,
        reportCallback: () -> Unit,
        deleteCallback: () -> Unit,
        updateCallback: () -> Unit,
        profileCallback: () -> Unit
    ) {
        val list = uid?.let {
            if (uid == UserInfo.userInfo?.uid ?: "" || UserInfo.userInfo?.admin == true) {
                OptionsSelectType.values().drop(2).map { getString(it.resID) }.toTypedArray()
            } else {
                OptionsSelectType.values().dropLast(2).map { getString(it.resID) }.toTypedArray()
            }
        } ?: OptionsSelectType.values().dropLast(2).map { getString(it.resID) }.toTypedArray()

        showListDialog(list, "") {
            when (list[it]) {
                getString(OptionsSelectType.REPORT.resID) -> reportCallback()
                getString(OptionsSelectType.DELETE.resID) -> deleteCallback()
                getString(OptionsSelectType.UPDATE.resID) -> updateCallback()
                getString(OptionsSelectType.LOOKUP.resID) -> profileCallback()
            }
        }
    }

    private fun BoardDetailViewModel.completeReport() {
        completeReportEvent.observe(this@BoardDetailActivity, Observer {
            showShortToast(getString(R.string.report_complete_message))
        })
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

    private fun moveToUserLookUpPage(uid:String){
        startAct(UserLookUpActivity::class, Bundle().apply {
            putString(UserLookUpActivity.USER_ID_KEY, uid)
        })
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

        if (BuildConfig.DEBUG) {
            MobileAds.setRequestConfiguration(
                RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf("ABCDEF012345"))
                    .build()
            )
        }

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
        adView?.adUnitId = getDetailBannerAdMobId(this)

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

                override fun onAdFailedToLoad(error: LoadAdError?) {
                    error?.let { Timber.e("Response SearchBanner Error == ${it.message}") }
                }
            }
        }

    }

    private fun showReportFragment(bid: String, title: String, userData: UserData) {
        ReportRegisterFragmentDialog.newInstance().apply {
            callbackAction = {
                viewModel.sendReport(
                    bid = bid,
                    title = title,
                    type = it.title!!,
                    userData = userData
                )
                dismiss()
            }
        }.show(supportFragmentManager, "")
    }

    private fun showConfirmDialog(message: String?, actionDialog: () -> Unit) {
        ConfirmDialog.newInstance(message = message).apply {
            confirmAction = { actionDialog() }
        }.show(supportFragmentManager, ConfirmDialog.TAG)
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
        const val KEY_BOARD_ID = "BOARD_ID"
        const val KEY_BOARD_DATA = "BOARD_DATA"
        const val BOARD_UPLOAD_REQ_CODE = 1055
        const val BOARD_UPLOAD_RES_CODE = 1056
        const val BOARD_REMOVE_RES_CODE = 1057
        const val BOARD_NOT_FOUND_RES_CODE = 1058
    }
}