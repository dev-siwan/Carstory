package com.like.drive.motorfeed.ui.report.reg.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.databinding.HolderReportRegisterItemBinding
import com.like.drive.motorfeed.ui.report.reg.data.ReportType
import com.like.drive.motorfeed.ui.report.reg.viewmodel.ReportRegisterViewModel

class ReportRegisterViewHolder(val binding: HolderReportRegisterItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: ReportRegisterViewModel, reportType: ReportType) {
        binding.data = reportType
        binding.vm = vm
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ReportRegisterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderReportRegisterItemBinding.inflate(layoutInflater, parent, false)

            return ReportRegisterViewHolder(binding)
        }
    }
}