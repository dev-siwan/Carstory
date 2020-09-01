package com.like.drive.motorfeed.ui.search.fragment

import android.app.Activity
import android.content.Intent
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
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.FragmentSearchBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.etc.PagingCallback
import com.like.drive.motorfeed.ui.base.ext.showListDialog
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.base.ext.withPaging
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.fragment.FeedListFragment
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.type.data.getFeedTypeList
import com.like.drive.motorfeed.ui.filter.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import com.like.drive.motorfeed.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.layout_search_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModel()
    private val feedListViewModel: FeedListViewModel by viewModel()
    private val feedAdapter by lazy { FeedListAdapter(feedListViewModel) }

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

        });

    }

    private fun withViewModel() {
        with(viewModel) {
            //showFilter()
            searchComplete()
           // showFeedType()
            pageToMotorType()
        }
        with(feedListViewModel) {
            listComplete()
            pageToDetailAct()
        }
    }

/*    private fun SearchViewModel.searchComplete() {
        searchBtnClickEvent.observe(viewLifecycleOwner, Observer {
            feedListViewModel.initDate(feedType.value, motorType.value, tagValue.value)
        })
    }
    */
    private fun SearchViewModel.searchComplete(){
        tagValueEvent.observe(viewLifecycleOwner, Observer {
            feedListViewModel.initDate(tagQuery = it)
        })
    }

    private fun FeedListViewModel.listComplete() {
        feedList.observe(viewLifecycleOwner, Observer {
            feedAdapter.run {
                if (isFirst) {
                    initList(it)
                } else {
                    moreList(it)
                }
            }
        })
    }

   /* private fun SearchViewModel.showFeedType() {
        filterFeedTypeClickEvent.observe(viewLifecycleOwner, Observer {
            getFeedTypeList(this@SearchFragment.requireContext()).toMutableList().apply {
                add(0, FeedTypeData(getString(R.string.not_select), "", 0))
            }.let {
                requireActivity().showListDialog(it.map { data -> data.title }
                    .toTypedArray()) { position ->
                    when (position) {
                        0 -> {
                            viewModel.setTypeFilter(null)
                        }
                        else -> {
                            viewModel.setTypeFilter(it[position])
                        }
                    }
                }
            }
        })
    }*/

   /* private fun visibleSearchView() {
        if (!listFilter.isVisible) {
            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 400
                addTarget(listFilter)
            }

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
            listFilter.visibility = View.VISIBLE
        }
    }

    private fun goneSearchView() {
        if (listFilter.isVisible) {
            val transition: Transition = Slide(Gravity.TOP)
            transition.apply {
                duration = 200
                addTarget(listFilter)
            }

            TransitionManager.beginDelayedTransition(rootView as ViewGroup, transition)
            listFilter.visibility = View.GONE
        }
    }
*/
    private fun SearchViewModel.pageToMotorType() {
        filterMotorTypeClickEvent.observe(viewLifecycleOwner, Observer {
            startActForResult(SelectMotorTypeActivity::class, SelectMotorTypeActivity.REQUEST_CODE)
        })
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

            SelectMotorTypeActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getParcelableExtra<MotorTypeData>(SelectMotorTypeActivity.RESULT_KEY)
                        ?.let {
                            viewModel.setMotorFilter(it)
                        }
                }
            }
        }
    }

}