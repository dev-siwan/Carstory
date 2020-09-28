package com.like.drive.motorfeed.ui.report.reg.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.like.drive.motorfeed.ui.report.reg.data.ReportType
import com.like.drive.motorfeed.ui.report.reg.holder.ReportRegisterViewHolder
import com.like.drive.motorfeed.ui.report.reg.viewmodel.ReportRegisterViewModel

class ReportTypeAdapter(val vm: ReportRegisterViewModel) :
    ListAdapter<ReportType, ReportRegisterViewHolder>(
        object : DiffUtil.ItemCallback<ReportType>() {
            override fun areItemsTheSame(oldItem: ReportType, newItem: ReportType) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: ReportType, newItem: ReportType) =
                oldItem.title == newItem.title

        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ReportRegisterViewHolder.from(parent)

    override fun onBindViewHolder(holder: ReportRegisterViewHolder, position: Int) {
        holder.bind(vm, getItem(position))
    }

}