package com.like.drive.motorfeed.ui.search.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel:SearchViewModel by viewModel()
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

}