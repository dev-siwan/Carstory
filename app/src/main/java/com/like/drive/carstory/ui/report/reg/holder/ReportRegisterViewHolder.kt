package com.like.drive.carstory.ui.report.reg.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.databinding.HolderReportRegisterItemBinding
import com.like.drive.carstory.ui.report.reg.data.ReportType
import com.like.drive.carstory.ui.report.reg.viewmodel.ReportRegisterViewModel

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