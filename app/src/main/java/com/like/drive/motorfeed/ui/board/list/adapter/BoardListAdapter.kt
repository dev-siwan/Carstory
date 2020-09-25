package com.like.drive.motorfeed.ui.board.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.ui.board.list.holder.ListAdvHolder
import com.like.drive.motorfeed.ui.board.list.holder.ListViewHolder
import com.like.drive.motorfeed.ui.board.list.viewmodel.BoardListViewModel

class BoardListAdapter(val vm: BoardListViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val feedList = mutableListOf<BoardData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADV -> ListAdvHolder.from(parent)
            else -> ListViewHolder.from(parent)
        }
    }

    override fun getItemCount() =
        feedList.size + feedList.size.div(ADV_POSITION)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> holder.bind(
                vm,
                feedList[position - position.div(5)]
            )
            is ListAdvHolder -> holder.bind()
        }
    }

    fun initList(boardList: List<BoardData>) {
        this.feedList.run {
            clear()
            addAll(boardList)
            notifyDataSetChanged()
        }
    }

    fun moreList(boardList: List<BoardData>) {
        this.feedList.run {
            val beforePosition = size + size.div(ADV_POSITION)
            addAll(boardList)
            notifyItemRangeInserted(beforePosition, boardList.size + beforePosition)
        }
    }

    fun addFeed(board: BoardData) {
        feedList.add(0, board)
        notifyItemInserted(0)
    }

    fun updateFeed(board: BoardData) {
        val originData = feedList.find { it.fid == board.fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList[index] = board
            val advIndex = index.div(ADV_POSITION)
            notifyItemChanged((index + advIndex))
        }
    }

    fun removeFeed(fid: String) {
        val originData = feedList.find { it.fid == fid }
        originData?.let {
            val index = feedList.indexOf(it)
            feedList.removeAt(index)
            val advIndex = index.div(ADV_POSITION)
            notifyItemRemoved((index + advIndex))
        }
    }

    fun getFeedData(fid: String) =
        feedList.find { it.fid == fid }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_ITEM
            position.rem(ADV_POSITION) == 0 -> TYPE_ADV
            else -> TYPE_ITEM
        }
    }

    companion object {
        const val ADV_POSITION = 5
        const val TYPE_ITEM = 1
        const val TYPE_ADV = 2
    }
}