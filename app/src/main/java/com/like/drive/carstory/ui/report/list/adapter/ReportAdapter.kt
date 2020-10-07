package com.like.drive.carstory.ui.report.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.report.ReportData
import com.like.drive.carstory.ui.report.list.holder.ReportViewHolder
import com.like.drive.carstory.ui.report.list.viewmodel.ReportViewModel

class ReportAdapter(val vm: ReportViewModel) : RecyclerView.Adapter<ReportViewHolder>() {

    private val reportDataList = mutableListOf<ReportData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder =
        ReportViewHolder.from(parent)

    override fun getItemCount() = reportDataList.size

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(vm, reportDataList[position])
    }

    fun initList(list: List<ReportData>) {
        this.reportDataList.run {
            clear()
            addAll(list)
            notifyDataSetChanged()
        }
    }

    fun moreList(list: List<ReportData>) {
        this.reportDataList.run {
            val beforePosition = size
            addAll(list)
            notifyItemRangeInserted(beforePosition, list.size + beforePosition)
        }
    }

    fun removeReport(rid: String?) {
        val originData = reportDataList.find { it.rid == rid }
        originData?.let {
            val index = reportDataList.indexOf(it)
            reportDataList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}