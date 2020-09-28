package com.like.drive.motorfeed.ui.notice.list.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.databinding.FragmentNoticeUploadDialogBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.base.ext.showShortToast
import com.like.drive.motorfeed.ui.notice.list.activity.NoticeListActivity
import com.like.drive.motorfeed.ui.notice.list.viewmodel.NoticeListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val NOTICE_DATA = "noticeData"

class NoticeUploadFragmentDialog :
    BaseFragmentDialog<FragmentNoticeUploadDialogBinding>(R.layout.fragment_notice_upload_dialog) {

    private val viewModel: NoticeListViewModel by viewModel()

    var onComplete: ((NoticeData, Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.initNoticeData(it.getParcelable(NOTICE_DATA))
        }
    }

    override fun onBind(dataBinding: FragmentNoticeUploadDialogBinding) {
        super.onBind(dataBinding)

        dataBinding.vm = viewModel
    }

    override fun onBindAfter(dataBinding: FragmentNoticeUploadDialogBinding) {
        super.onBindAfter(dataBinding)

        withViewModel()

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
    }

    fun withViewModel() {
        with(viewModel) {
            complete()
            error()
            loading()
        }
    }

    private fun NoticeListViewModel.complete() {
        addCompleteEvent.observe(viewLifecycleOwner, Observer { completeData ->

            viewModel.noticeData?.let { onComplete?.invoke(completeData, true) }
                ?: onComplete?.invoke(completeData, false)

        })
    }

    private fun NoticeListViewModel.error() {
        errorEvent.observe(viewLifecycleOwner, Observer {
            requireContext().showShortToast(it)
        })
    }

    private fun NoticeListViewModel.loading() {
        loadingEvent.observe(viewLifecycleOwner, Observer {
            (requireActivity() as NoticeListActivity).loadingStatus(it)
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(noticeData: NoticeData? = null) =
            NoticeUploadFragmentDialog().apply {
                arguments = Bundle().apply {
                    putParcelable(NOTICE_DATA, noticeData)
                }
            }
    }
}