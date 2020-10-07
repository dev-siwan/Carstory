package com.like.drive.carstory.ui.report.list.holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.report.ReportData
import com.like.drive.carstory.databinding.HolderReportListBinding
import com.like.drive.carstory.ui.report.list.viewmodel.ReportViewModel

class ReportViewHolder(val binding: HolderReportListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vm: ReportViewModel, data: ReportData) {
        binding.reportData = data
        binding.vm = vm
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): ReportViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = HolderReportListBinding.inflate(layoutInflater, parent, false)

            return ReportViewHolder(binding)
        }
    }
}