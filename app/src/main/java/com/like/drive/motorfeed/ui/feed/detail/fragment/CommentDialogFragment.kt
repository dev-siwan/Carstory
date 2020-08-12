package com.like.drive.motorfeed.ui.feed.detail.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentReCommentDialogBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.feed.data.CommentFragmentExtra
import com.like.drive.motorfeed.ui.feed.detail.viewmodel.FeedDetailViewModel
import kotlinx.android.synthetic.main.fragment_re_comment_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


private const val EXTRA_PARAM = "extraParam"

class CommentDialogFragment :
    BaseFragmentDialog<FragmentReCommentDialogBinding>(R.layout.fragment_re_comment_dialog) {
    private var commentFragmentExtra: CommentFragmentExtra? = null
    private val feedDetailViewModel: FeedDetailViewModel by sharedViewModel()
    private val imm by lazy{requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            commentFragmentExtra = it.getParcelable(EXTRA_PARAM)
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.run {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        commentFragmentExtra?.run {
            commentData?.let {
                if (commentUpdate == true) {
                    feedDetailViewModel.setReComment(it.commentStr)
                }
            }
            reCommentData?.let {
                if (commentUpdate == true) {
                    feedDetailViewModel.setReComment(it.commentStr)
                }
            }

            isCancelable = true

            containerFragment.setOnClickListener {
                dismiss()
            }

            lifecycleScope.launch(Dispatchers.Main) {
                delay(50)
                etComment.requestFocus()

                imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }

            withViewModel()

        } ?: notFoundData()

    }

    private fun notFoundData(){
        requireContext().showShortToast(R.string.not_found_data)
        dismiss()
    }

    private fun withViewModel(){
        with(feedDetailViewModel){
            complete()
        }
    }

    private fun FeedDetailViewModel.complete(){
        completeCommentDialogEvent.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
    }


    override fun dismiss() {
        super.dismiss()
        feedDetailViewModel.reComment.value = null
        imm?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0)
    }


    override fun onBind(dataBinding: FragmentReCommentDialogBinding) {
        super.onBind(dataBinding)
        dataBinding.commentFragmentExtra = commentFragmentExtra
        dataBinding.vm = feedDetailViewModel
    }

    companion object {
        @JvmStatic
        fun newInstance(commentFragmentExtra: CommentFragmentExtra) =
            CommentDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_PARAM, commentFragmentExtra)
                }
            }
    }
}