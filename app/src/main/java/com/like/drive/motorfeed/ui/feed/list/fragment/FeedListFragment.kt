package com.like.drive.motorfeed.ui.feed.list.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.databinding.FragmentFeedListBinding
import com.like.drive.motorfeed.ui.base.BaseFragment
import com.like.drive.motorfeed.ui.base.ext.startActForResult
import com.like.drive.motorfeed.ui.feed.detail.activity.FeedDetailActivity
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.list.dialog.FeedListFilterDialog
import com.like.drive.motorfeed.ui.feed.list.viewmodel.FeedListViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.feed.upload.activity.FeedUploadActivity
import com.like.drive.motorfeed.ui.main.activity.MainActivity
import com.like.drive.motorfeed.ui.main.viewmodel.MainViewModel
import com.like.drive.motorfeed.ui.motor.activity.SelectMotorTypeActivity
import kotlinx.android.synthetic.main.fragment_feed_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedListFragment : BaseFragment<FragmentFeedListBinding>(R.layout.fragment_feed_list) {

    private val viewModel: FeedListViewModel by viewModel()
    private val feedListAdapter by lazy { FeedListAdapter(viewModel) }
    private val filterDialog by lazy { FeedListFilterDialog }

    override fun onBind(dataBinding: FragmentFeedListBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
        dataBinding.rvFeedList.adapter = feedListAdapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        withViewModel()
    }

    private fun initView() {
        val decorationItem = DividerItemDecoration(
            requireContext(), DividerItemDecoration.VERTICAL
        ).apply {
            ContextCompat.getDrawable(
                requireContext(), R.drawable.line_solid_grey_6
            )?.let { setDrawable(it) }
        }
        rvFeedList?.run {
            addItemDecoration(decorationItem)
        }

        initData()
    }

    private fun initData(){
        if(feedListAdapter.feedList.isEmpty()){
            viewModel.getFeedList()
        }
    }

    private fun withViewModel() {
        with(viewModel) {
            pageToDetailAct()
            showFilter()
            feedList()
        }

    }

    private fun FeedListViewModel.feedList() {
        feedList.observe(viewLifecycleOwner, Observer {
            feedListAdapter.initList(it)
        })
    }

    private fun FeedListViewModel.pageToDetailAct() {
        feedItemClickEvent.observe(viewLifecycleOwner, Observer {
            startForResult(FeedDetailActivity::class, FEED_LIST_TO_DETAIL_REQ, Bundle().apply {
                putString(FeedDetailActivity.KEY_FEED_ID, it)
            })
        })
    }

    private fun FeedListViewModel.showFilter() {
        filterClickEvent.observe(viewLifecycleOwner, Observer {
            filterDialog.newInstance().apply {
                setFilter = { feedType, motorType ->
                    viewModel.setFilter(feedType, motorType)
                    dismiss()
                }
            }.show(requireActivity().supportFragmentManager, "")
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FEED_LIST_TO_DETAIL_REQ -> {
                when (resultCode) {
                    FeedDetailActivity.FEED_UPLOAD_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedListAdapter.updateFeed(it)
                        }
                    }
                    FeedDetailActivity.FEED_REMOVE_RES_CODE -> {
                        data?.getParcelableExtra<FeedData>(FeedDetailActivity.KEY_FEED_DATA)?.let {
                            feedListAdapter.removeFeed(it)
                        }
                    }
                }
            }

            MainActivity.UPLOAD_FEED_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        data?.getParcelableExtra<FeedData>(FeedUploadActivity.FEED_CREATE_KEY)
                            ?.let {
                                feedListAdapter.run {
                                   addFeed(it)
                                }
                            }
                    }
                }
            }
        }
    }

    companion object {
        const val FEED_LIST_TO_DETAIL_REQ = 1515
    }
}
