package com.like.drive.motorfeed.ui.search.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.gms.ads.*
import com.google.android.material.appbar.AppBarLayout
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.hideKeyboard
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.search.adapter.RecentlyListAdapter
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_recently_search_list.view.*
import kotlinx.android.synthetic.main.layout_search_list.*
import kotlinx.android.synthetic.main.layout_search_list.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModel()
    private val feedListViewModel: FeedListViewModel by viewModel()
    private val feedAdapter by lazy { FeedListAdapter(feedListViewModel) }
    private val editFocusListener by lazy {
        View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                visibleSearchView()
            }
        }
    }
    private val imm by lazy { requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? }
    private lateinit var onCallback: OnBackPressedCallback
    private val params by lazy { containerSearch.layoutParams as AppBarLayout.LayoutParams }
    private val appbarPrams by lazy { appBarLayout.layoutParams as CoordinatorLayout.LayoutParams }
    private var isScroll = false


    private var adView:AdView?=null
    private var initialLayoutComplete = false
    private val adSize: AdSize
        get() {
            val display = requireActivity().windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

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
        dataBinding.feedVm = feedListViewModel
        dataBinding.incSearchList.rvFeed.adapter = feedAdapter
        dataBinding.incRecentlyList.rvRecently.adapter = RecentlyListAdapter(viewModel)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()

    }

    private fun initView() {

        //광고 초기화
        adView?:advInit()

        //리싸이클러뷰 페이징
        rvFeed.apply {
            paging()

        }

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

            incSearchList.swipeLayout.setOnRefreshListener {
                setAppbarNotScroll()
                isScroll = false
                feedListViewModel.loadingStatus = FeedListViewModel.LoadingStatus.REFRESH
                feedListViewModel.initDate(tagQuery = viewModel.tag.value)
            }
        }

        //피드 리스트 유무에 따른 검색창 visible/gone
        if (feedAdapter.feedList.isEmpty()) {
            etSearch.requestFocus()
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            goneSearchView()
        }

        ivBackButton.setOnClickListener {
            goneSearchView()
        }

    }

    private fun RecyclerView.paging() {

        withPaging(object : PagingCallback {
            override fun requestMoreList() {

                with(feedListViewModel) {
                    if (!getLastDate()) {
                        feedList.value?.lastOrNull()?.createDate?.let {
                            moreData(it)
                        }
                    }
                }
            }

            override fun isRequest(): Boolean = false

        })

    }

    private fun withViewModel() {
        with(viewModel) {
            searchComplete()
            tagNullBlankWarningMessage()
        }
        with(feedListViewModel) {
            listComplete()
            pageToDetailAct()
        }
    }

    private fun SearchViewModel.searchComplete() {
        tagValueEvent.observe(viewLifecycleOwner, Observer {
            setAppbarNotScroll()
            isScroll = false
            feedListViewModel.loadingStatus = FeedListViewModel.LoadingStatus.INIT
            feedListViewModel.initDate(tagQuery = it)
            goneSearchView()
        })
    }

    private fun SearchViewModel.tagNullBlankWarningMessage() {
        tagBlankMessageEvent.observe(viewLifecycleOwner, Observer {
            requireContext().showShortToast(it)
        })
    }

    private fun FeedListViewModel.listComplete() {
        feedList.observe(viewLifecycleOwner, Observer {
            feedAdapter.run {
                if (isFirst) {
                    initList(it)
                    if (it.isNotEmpty()) {
                        setAppbarScroll()
                        isScroll = true
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

            onCallback.isEnabled = feedAdapter.feedList.isNotEmpty()
            setAppbarNotScroll()

            viewModel.isSearchStatus.set(true)
        }
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

            onCallback.isEnabled = false

            viewModel.isSearchStatus.set(false)

            if (feedAdapter.feedList.isNotEmpty() && isScroll) {
                setAppbarScroll()
            }

        }
    }

    private fun FeedListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startForResult(
                FeedDetailActivity::class,
                FeedListFragment.FEED_LIST_TO_DETAIL_REQ, Bundle().apply {
                    putString(FeedDetailActivity.KEY_FEED_ID, it)
                })
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FeedListFragment.FEED_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    FeedDetailActivity.FEED_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedAdapter.removeFeed(it)
                        }
                    }
                }
            }
        }
    }

    private fun setAppbarScroll() {
        params.scrollFlags =
                AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
            appbarPrams.behavior = AppBarLayout.Behavior()
            appBarLayout.layoutParams = appbarPrams

    }

    private fun setAppbarNotScroll() {
        params.scrollFlags = 0
        appbarPrams.behavior = null
        appBarLayout.layoutParams = appbarPrams
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
        super.onDestroyView()
    }

    private fun advInit(){

        initialLayoutComplete=false

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
        adView?.adUnitId = "ca-app-pub-3940256099942544/9214589741"

        adView?.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView?.loadAd(adRequest)

        adView?.run {
            setAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    incRecentlyList.containerAdv.isVisible = true

                }
            })
        }

    }
}