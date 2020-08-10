package com.like.drive.motorfeed.ui.feed.detail.fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.databinding.FragmentReCommentDialogBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import kotlinx.android.synthetic.main.fragment_re_comment_dialog.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


private const val COMMENT_DATA_PARAM = "commentData"

class ReCommentDialogFragment :
    BaseFragmentDialog<FragmentReCommentDialogBinding>(R.layout.fragment_re_comment_dialog) {
    private var commentData: CommentData? = null
    private val feedDetailViewModel: FeedDetailViewModel by sharedViewModel()
    private var originalMode : Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            commentData = it.getParcelable(COMMENT_DATA_PARAM)
        }


        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        isCancelable = true

        containerFragment.setOnClickListener {
            dismiss()
        }


    }


    override fun onBind(dataBinding: FragmentReCommentDialogBinding) {
        super.onBind(dataBinding)
        dataBinding.commentData = commentData
        dataBinding.vm = feedDetailViewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(commentData: CommentData) =
            ReCommentDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(COMMENT_DATA_PARAM, commentData)
                }
            }
    }
}