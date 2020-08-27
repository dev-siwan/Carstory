package com.like.drive.motorfeed.ui.search.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_search_list.*
import kotlinx.android.synthetic.main.layout_search_list.view.*
import kotlinx.android.synthetic.main.layout_search_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModel()
    private val feedListViewModel: FeedListViewModel by viewModel()
    private val feedAdapter by lazy { FeedListAdapter(feedListViewModel) }
    private val filterDialog by lazy { FeedListFilterDialog }

    override fun onBind(dataBinding: FragmentSearchBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.feedVm = feedListViewModel
        dataBinding.incSearchList.rvFeed.adapter = feedAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    private fun initView() {

        rvFeed.apply {
            paging()
        }

        if(incSearchList.isVisible){
            cvSearchView.visibility = View.GONE
        }else{
            cvSearchView.visibility = View.VISIBLE
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

        }, scrollDown = {



        })
    }

    private fun withViewModel() {
        with(viewModel) {
            showFilter()
            searchComplete()
            searchIcEvent()
        }
        with(feedListViewModel) {
            listComplete()
        }
    }

    private fun SearchViewModel.showFilter() {
        filterClickEvent.observe(viewLifecycleOwner, Observer {
            filterDialog.newInstance(viewModel.feedType.value, viewModel.motorType.value).apply {
                setFilter = { feedType, motorType ->
                    dismiss()
                    this@SearchFragment.viewModel.setFilter(feedType, motorType)
                }
            }.show(requireActivity().supportFragmentManager, "")
        })
    }

    private fun SearchViewModel.searchComplete() {
        searchBtnClickEvent.observe(viewLifecycleOwner, Observer {
            feedListViewModel.initDate(feedType.value, motorType.value, tagValue.value)
        })
    }

    private fun SearchViewModel.searchIcEvent() {
        searchIcClickEvent.observe(viewLifecycleOwner, Observer {
            visibleSearchView()
            goneListTop()
        })
    }

    private fun FeedListViewModel.listComplete() {
        feedList.observe(viewLifecycleOwner, Observer {
            feedAdapter.run {
                if (isFirst) {
                    initList(it)
                    goneSearchView()
                    visibleListTop()
                } else {
                    moreList(it)
                }
            }
        })
    }

    private fun visibleSearchView() {

        val transition: Transition = Slide(Gravity.TOP)
        transition.apply {
            duration = 400
            addTarget(incSearchView)
        }

        TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
        incSearchView.visibility = View.VISIBLE

    }

    private fun goneSearchView() {

        val transition: Transition = Slide(Gravity.TOP)
        transition.apply {
            duration = 200
            addTarget(incSearchView)
        }

        TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
        incSearchView.visibility = View.GONE

    }

    private fun goneListTop() {
        lifecycleScope.launch(Dispatchers.Main) {

            val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
            incSearchList.startAnimation(slideDown)
            incSearchList.visibility = View.GONE

        }
    }

    private fun visibleListTop() {
        lifecycleScope.launch(Dispatchers.Main) {
            val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            incSearchList.startAnimation(slideUp)
            incSearchList.visibility = View.VISIBLE

        }
    }

}