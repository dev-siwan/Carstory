package com.like.drive.carstory.ui.search.fragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.gms.ads.*
import com.like.drive.carstory.R
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.databinding.FragmentSearchBinding
import com.like.drive.carstory.ui.base.BaseFragment
import com.like.drive.carstory.ui.base.etc.PagingCallback
import com.like.drive.carstory.ui.base.ext.*
import com.like.drive.carstory.ui.board.detail.activity.BoardDetailActivity
import com.like.drive.carstory.ui.board.list.activity.BoardListActivity
import com.like.drive.carstory.ui.board.list.adapter.BoardListAdapter
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel
import com.like.drive.carstory.ui.common.data.LoadingStatus
import com.like.drive.carstory.ui.main.activity.MainActivity
import com.like.drive.carstory.ui.search.adapter.RecentlyListAdapter
import com.like.drive.carstory.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_recently_search_list.view.*
import kotlinx.android.synthetic.main.layout_search_list.*
import kotlinx.android.synthetic.main.layout_search_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModel()
    private val boardListViewModel: BoardListViewModel by viewModel()
    private val feedAdapter by lazy { BoardListAdapter(boardListViewModel) }
    private val editFocusListener by lazy {
        View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                visibleSearchView()
            }
        }
    }
    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? }
    private lateinit var onCallback: OnBackPressedCallback
    private var adView: AdView? = null
    private var initialLayoutComplete = false

    private var tagValue: String? = null

    @Suppress("DEPRECATION")
    private val adSize: AdSize
        get() {
            val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().display
            } else {
                requireActivity().windowManager.defaultDisplay
            }
            val outMetrics = DisplayMetrics()
            display?.getRealMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = incRecentlyList.containerAdv.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getPortraitAnchoredAdaptiveBannerAdSize(requireContext(), adWidth)
        }

    override fun onBind(dataBinding: FragmentSearchBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.boardVm = boardListViewModel
        dataBinding.incSearchList.rvBoard.adapter = feedAdapter
        dataBinding.incRecentlyList.rvRecently.adapter = RecentlyListAdapter(viewModel)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()

    }

    private fun initView() {

        //광고 초기화
        adView ?: advInit()

        //리싸이클러뷰 페이징
        rvBoard.init()

        //검색창 포커스
        etSearch.onFocusChangeListener = editFocusListener

        requireActivity().apply {
            //뒤로 가기 버튼 눌렸을 때 검색창이 뜨면 닫는다.
            onCallback = onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                if (isEnabled) {
                    goneSearchView()
                }
            }

            //검색창 소프트 키보드 올라왔을때 바텀네비 올라오는거 막기
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        }

        //새로고침 리스너
        incSearchList.swipeLayout.listener()

        //피드 리스트 유무에 따른 검색창 visible/gone
        isListItemEmpty()

        //뒤로가기 버튼
        ivBackButton.init()

    }

    private fun RecyclerView.init() {

        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(boardListViewModel) {
                    if (!getLastDate()) {
                        boardList.value?.lastOrNull()?.createDate?.let {
                            moreData(it)
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })

        addItemDecoration(dividerItemDecoration())
    }

    private fun AppCompatImageView.init() {

        setOnClickListener {
            if (feedAdapter.boardList.isNotEmpty()) {
                goneSearchView()
                return@setOnClickListener
            }

            rootView.requestFocus()
            requireActivity().hideKeyboard(rootView)
            moveHome()

        }
    }

    private fun SwipeRefreshLayout.listener() {
        setOnRefreshListener {
            boardListViewModel.loadingStatus = LoadingStatus.REFRESH
            boardListViewModel.initData(tagQuery = viewModel.tag.value)
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            searchComplete()
            tagNullBlankWarningMessage()
        }
        with(boardListViewModel) {
            listComplete()
            pageToDetailAct()
        }
    }

    private fun SearchViewModel.searchComplete() {
        tagValueEvent.observe(viewLifecycleOwner, Observer {
            boardListViewModel.loadingStatus = LoadingStatus.INIT
            boardListViewModel.initData(tagQuery = it)
            goneSearchView()
        })
    }

    private fun SearchViewModel.tagNullBlankWarningMessage() {
        tagBlankMessageEvent.observe(viewLifecycleOwner, Observer {
            requireContext().showShortToast(it)
        })
    }

    private fun BoardListViewModel.listComplete() {
        boardList.observe(viewLifecycleOwner, Observer {

            feedAdapter.run {
                if (isFirst) {
                    initList(it)
                    if (it.isNotEmpty()) {
                        tagValue = viewModel.tag.value
                    }

                } else {
                    moreList(it)
                }
            }
        })
    }

    private fun visibleSearchView() {
        if (!incRecentlyList.isVisible) {

            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 400
                addTarget(incRecentlyList)
            }

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
            incRecentlyList.visibility = View.VISIBLE

        }
        onCallback.isEnabled = feedAdapter.boardList.isNotEmpty()

        viewModel.isSearchStatus.set(true)
    }

    private fun goneSearchView() {
        if (incRecentlyList.isVisible) {

            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 200
                addTarget(incRecentlyList)
            }

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
            incRecentlyList.visibility = View.GONE

            rootView.requestFocus()
            requireActivity().hideKeyboard(rootView)
        }
        onCallback.isEnabled = false

        viewModel.isSearchStatus.set(false)
    }

    private fun BoardListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startForResult(
                BoardDetailActivity::class,
                BoardListActivity.BOARD_LIST_TO_DETAIL_REQ, Bundle().apply {
                    putString(BoardDetailActivity.KEY_BOARD_ID, it)
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            BoardListActivity.BOARD_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    BoardDetailActivity.BOARD_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<BoardData>(BoardDetailActivity.KEY_BOARD_DATA)
                            ?.let {
                                feedAdapter.updateBoard(it)
                            }
                    }
                    BoardDetailActivity.BOARD_REMOVE_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            feedAdapter.removeBoard(it)
                        }
                    }
                    BoardDetailActivity.BOARD_NOT_FOUND_RES_CODE -> {
                        data?.getStringExtra(BoardDetailActivity.KEY_BOARD_DATA)?.let {
                            feedAdapter.removeBoard(it)
                        }
                    }
                }
            }
        }
    }

    private fun moveHome() {
        (requireActivity() as MainActivity).navBottomView.selectedItemId = R.id.action_home
    }

    override fun onResume() {
        adView?.resume()
        super.onResume()
    }

    override fun onPause() {
        adView?.pause()
        super.onPause()
    }

    override fun onDestroyView() {
        adView?.destroy()
        adView = null

        viewModel.tag.value = tagValue

        super.onDestroyView()
    }

    private fun advInit() {

        initialLayoutComplete = false

        MobileAds.initialize(requireContext())

        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )

        adView = AdView(requireContext())
        incRecentlyList.containerAdv.apply {
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
        adView?.adUnitId = getBannerAdMobId(requireContext())

        adView?.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView?.loadAd(adRequest)

        adView?.run {
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    incRecentlyList.containerAdv.isVisible = true

                }
            }
        }

    }

    private fun isListItemEmpty() {
        //피드 리스트 유무에 따른 검색창 visible/gone
        if (feedAdapter.boardList.isEmpty()) {
            etSearch.requestFocus()
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            viewModel.tag.value = null
        } else {
            goneSearchView()
        }
    }
}