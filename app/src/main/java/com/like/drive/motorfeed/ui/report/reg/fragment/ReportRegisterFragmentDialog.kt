package com.like.drive.motorfeed.ui.report.reg.fragment

import androidx.lifecycle.Observer
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.databinding.FragmentReportRegisterDialogBinding
import com.like.drive.motorfeed.ui.base.BaseFragmentDialog
import com.like.drive.motorfeed.ui.dialog.ConfirmDialog
import com.like.drive.motorfeed.ui.report.reg.adapter.ReportTypeAdapter
import com.like.drive.motorfeed.ui.report.reg.data.ReportType
import com.like.drive.motorfeed.ui.report.reg.viewmodel.ReportRegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReportRegisterFragmentDialog :
    BaseFragmentDialog<FragmentReportRegisterDialogBinding>(R.layout.fragment_report_register_dialog) {

    private val viewModel: ReportRegisterViewModel by viewModel()
    private val reportTypeAdapter by lazy { ReportTypeAdapter(viewModel) }
    var callbackAction: ((ReportType) -> Unit)? = null

    override fun onBind(dataBinding: FragmentReportRegisterDialogBinding) {
        dataBinding.vm = viewModel
        dataBinding.rvReportTypeList.run {
            adapter = reportTypeAdapter
        }

    }

    override fun onBindAfter(dataBinding: FragmentReportRegisterDialogBinding) {
        withViewModel()
    }

    private fun withViewModel() {
        with(viewModel) {
            reportTypeList()
            completeItem()
        }
    }

    private fun ReportRegisterViewModel.reportTypeList() {
        reportTypes.observe(viewLifecycleOwner, Observer {
            reportTypeAdapter.submitList(it)
        })
    }

    private fun ReportRegisterViewModel.completeItem() {
        onCompleteEvent.observe(viewLifecycleOwner, Observer {

            ConfirmDialog.newInstance(message = "${it.title}(으)로 신고하시겠습니까?").apply {
                confirmAction = {
                    callbackAction?.invoke(it)
                }
            }.show(requireActivity().supportFragmentManager, ConfirmDialog.TAG)

        })
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ReportRegisterFragmentDialog()
    }
}