package com.like.drive.motorfeed.ui.search.fragment

import android.os.Bundle

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel:SearchViewModel by viewModel()
    private val feedListViewModel:FeedListViewModel by viewModel()
    private val feedAdapter by lazy { FeedListAdapter(feedListViewModel) }
    private val filterDialog by lazy { FeedListFilterDialog }


    override fun onBind(dataBinding: FragmentSearchBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
        dataBinding.feedVm = feedListViewModel
        dataBinding.rvFeed.adapter = feedAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        withViewModel()
    }

    private fun withViewModel(){
        with(viewModel){
            showFilter()
            searchComplete()
        }
        with(feedListViewModel){
            listComplete()
        }
    }

    private fun SearchViewModel.showFilter() {
        filterClickEvent.observe(viewLifecycleOwner, Observer {
            filterDialog.newInstance(viewModel.feedType.value,viewModel.motorType.value).apply {
                setFilter = { feedType, motorType ->
                    dismiss()
                    this@SearchFragment.viewModel.setFilter(feedType, motorType)
                }
            }.show(requireActivity().supportFragmentManager, "")
        })
    }

    private fun SearchViewModel.searchComplete(){
        searchBtnClickEvent.observe(viewLifecycleOwner, Observer {
            feedListViewModel.getFeedList(feedType.value,motorType.value,tagValue.value)
        })
    }


    private fun FeedListViewModel.listComplete() {
        feedList.observe(viewLifecycleOwner, Observer {
            goneSearchView(cvSearchView) {
                listVisible(containerList)
            }
        })
    }


    private fun goneSearchView(view:View, action:()->Unit) {
        val transition: Transition = Slide(Gravity.TOP)
        transition.apply {
            duration = 200
            addTarget(view)
        }

        TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
        view.visibility = View.GONE
        action()
    }


    private fun listVisible(view:View) {
        view.visibility=View.VISIBLE
        val slideUp = AnimationUtils.loadAnimation(context,R.anim.slide_up)
        view.startAnimation(slideUp)
    }


}