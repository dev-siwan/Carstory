package com.like.drive.carstory.ui.board.detail.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.like.drive.carstory.R
import com.like.drive.carstory.databinding.FragmentCommentDialogBinding
import com.like.drive.carstory.ui.base.BaseFragmentDialog
import com.like.drive.carstory.ui.base.ext.setOnScrollableTouchListener
import com.like.drive.carstory.ui.base.ext.showShortToast
import com.like.drive.carstory.ui.board.data.CommentFragmentExtra
import com.like.drive.carstory.ui.board.detail.viewmodel.BoardDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_comment_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val EXTRA_PARAM = "extraParam"

@AndroidEntryPoint
class CommentDialogFragment :
    BaseFragmentDialog<FragmentCommentDialogBinding>(R.layout.fragment_comment_dialog) {
    private var commentFragmentExtra: CommentFragmentExtra? = null
    private val boardDetailViewModel: BoardDetailViewModel by activityViewModels()
    private val imm by lazy { requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager? }

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
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        commentFragmentExtra?.run {

            commentData?.let {
                if (commentUpdate == true) {
                    boardDetailViewModel.setReComment(it.commentStr)
                } else {
                    boardDetailViewModel.setReComment(null)
                }
            }
            reCommentData?.let {
                if (commentUpdate == true) {
                    boardDetailViewModel.setReComment(it.commentStr)
                } else {
                    boardDetailViewModel.setReComment(null)
                }
            }

            reCommentReply?.let {
                boardDetailViewModel.setReComment(null)
            }

            containerFragment.setOnClickListener {
                dismiss()
            }

            lifecycleScope.launch(Dispatchers.Main) {
                delay(50)
                etComment.requestFocus()

                imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }

            etComment.setOnScrollableTouchListener()

            withViewModel()

        } ?: notFoundData()

    }

    private fun notFoundData() {
        requireContext().showShortToast(R.string.not_found_data)
        dismiss()
    }

    private fun withViewModel() {
        with(boardDetailViewModel) {
            complete()
        }
    }

    private fun BoardDetailViewModel.complete() {
        completeCommentDialogEvent.observe(viewLifecycleOwner, Observer {
            dismiss()
        })
    }

    override fun dismiss() {
        super.dismiss()
        boardDetailViewModel.reComment.value = null
        imm?.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
    }

    override fun onBind(dataBinding: FragmentCommentDialogBinding) {
        super.onBind(dataBinding)
        dataBinding.commentFragmentExtra = commentFragmentExtra
        dataBinding.vm = boardDetailViewModel
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