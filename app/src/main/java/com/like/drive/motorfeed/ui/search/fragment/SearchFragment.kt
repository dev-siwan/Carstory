package com.like.drive.motorfeed.ui.search.fragment

import android.os.Bundle

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.transition.Scene
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel:SearchViewModel by viewModel()
    private val feedListViewModel:FeedListViewModel by viewModel()
    private val filterDialog by lazy { FeedListFilterDialog }


    override fun onBind(dataBinding: FragmentSearchBinding) {
        super.onBind(dataBinding)
        dataBinding.vm = viewModel
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


    private fun FeedListViewModel.listComplete(){
        feedList.observe(viewLifecycleOwner, Observer {
            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 200
                addTarget(cvSearchView)
            }

            val aScene:Scene= Scene.getSceneForLayout(rootView as ViewGroup,R.layout.fragment_search,requireContext())
            TransitionManager.go(aScene, transition)
            cvSearchView.visibility=View.GONE
        })
    }


}