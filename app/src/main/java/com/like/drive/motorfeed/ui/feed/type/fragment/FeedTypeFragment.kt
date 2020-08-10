package com.like.drive.motorfeed.ui.feed.type.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentFeedTypeBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.feed.type.adapter.FeedTypeAdapter
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeItem
import com.like.drive.motorfeed.ui.feed.type.data.getFeedTypeList
import com.like.drive.motorfeed.ui.feed.upload.viewmodel.FeedUploadViewModel
import kotlinx.android.synthetic.main.fragment_feed_type.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class FeedTypeFragment : BaseFragmentDialog<FragmentFeedTypeBinding>(R.layout.fragment_feed_type) {
    private val viewModel:FeedUploadViewModel by sharedViewModel()
    val list by lazy { getFeedTypeList(requireContext())}


    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isCancelable = true

        containerFragment.setOnClickListener {
            dismiss()
        }

    }


    override fun onBind(dataBinding: FragmentFeedTypeBinding) {
        super.onBind(dataBinding)
        dataBinding.rvFeedType.adapter = FeedTypeAdapter(list,viewModel)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeedTypeFragment()
    }
}