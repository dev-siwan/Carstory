package com.like.drive.motorfeed.ui.notice.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.ui.notice.list.holder.NoticeListViewHolder
import com.like.drive.motorfeed.ui.notice.list.viewmodel.NoticeListViewModel

class NoticeListAdapter(val vm:NoticeListViewModel) : RecyclerView.Adapter<NoticeListViewHolder>() {

    val noticeList = mutableListOf<NoticeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeListViewHolder =
        NoticeListViewHolder.from(parent)

    override fun getItemCount(): Int = noticeList.size

    override fun onBindViewHolder(holder: NoticeListViewHolder, position: Int) {
        holder.bind(vm,noticeList[position])
    }

    fun initList(noticeList: List<NoticeData>) {
        this.noticeList.run {
            clear()
            addAll(noticeList)
            notifyDataSetChanged()
        }
    }

    fun moreList(noticeList: List<NoticeData>) {
        this.noticeList.run {
            val beforePosition = size
            addAll(noticeList)
            notifyItemRangeInserted(beforePosition, noticeList.size + beforePosition)
        }
    }

    fun addFeed(noticeData: NoticeData) {
        noticeList.add(0, noticeData)
        notifyItemInserted(0)
    }

    fun updateFeed(noticeData: NoticeData) {
        val originData = noticeList.find { it.gid == noticeData.gid }
        originData?.let {
            val index = noticeList.indexOf(it)
            noticeList[index] = noticeData
            notifyItemChanged(index)
        }
    }

    fun removeFeed(gid: String) {
        val originData = noticeList.find { it.gid == gid }
        originData?.let {
            val index = noticeList.indexOf(it)
            noticeList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

}