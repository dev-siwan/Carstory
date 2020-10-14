package com.like.drive.carstory.ui.board.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.board.BoardWrapperData
import com.like.drive.carstory.ui.board.list.holder.ListAdvHolder
import com.like.drive.carstory.ui.board.list.holder.ListViewHolder
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel

class BoardListAdapter(val vm: BoardListViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val boardList = mutableListOf<BoardWrapperData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADV -> ListAdvHolder.from(parent)
            else -> ListViewHolder.from(parent)
        }
    }

    override fun getItemCount() =
        boardList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> holder.bind(vm, boardList[position].boardData)
            is ListAdvHolder -> holder.bind(boardList[position].nativeAd)
        }
    }

    fun initList(boardList: List<BoardData>) {
        this.boardList.run {
            clear()
            addAll(boardList.map { BoardWrapperData(boardData = it) })
            notifyDataSetChanged()
        }
    }

    fun moreList(boardList: List<BoardData>) {
        this.boardList.run {
            val beforePosition = size
            addAll(boardList.map { BoardWrapperData(boardData = it) })
            notifyItemRangeInserted(beforePosition, boardList.size + beforePosition)
        }
    }

    fun addBoard(board: BoardData) {
        boardList.add(0, BoardWrapperData(boardData = board))
        notifyItemInserted(0)
    }

    fun addAd(index: Int, ad: UnifiedNativeAd) {
        boardList.add(index, BoardWrapperData(nativeAd = ad))
        notifyItemInserted(index)
    }

    fun updateBoard(board: BoardData) {
        val originData = boardList.find { it.boardData?.bid == board.bid }
        originData?.let {
            val index = boardList.indexOf(it)
            boardList[index].boardData = board
            notifyItemChanged(index)
        }
    }

    fun removeBoard(bid: String) {
        val originData = boardList.find { it.boardData?.bid == bid }
        originData?.let {
            val index = boardList.indexOf(it)
            boardList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getBoardData(bid: String) =
        boardList.find { it.boardData?.bid == bid }?.boardData

    override fun getItemViewType(position: Int): Int {
        return when {
            boardList[position].boardData != null -> TYPE_ITEM
            else -> TYPE_ADV
        }
    }

    companion object {

        const val TYPE_ITEM = 0
        const val TYPE_ADV = 1
    }
}