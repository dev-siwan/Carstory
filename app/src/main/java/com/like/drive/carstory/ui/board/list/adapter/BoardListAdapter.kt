package com.like.drive.carstory.ui.board.list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.ui.board.list.holder.ListAdvHolder
import com.like.drive.carstory.ui.board.list.holder.ListViewHolder
import com.like.drive.carstory.ui.board.list.viewmodel.BoardListViewModel

class BoardListAdapter(val vm: BoardListViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val boardList = mutableListOf<BoardData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADV -> ListAdvHolder.from(parent)
            else -> ListViewHolder.from(parent)
        }
    }

    override fun getItemCount() =
        boardList.size + boardList.size.div(ADV_POSITION)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ListViewHolder -> holder.bind(
                vm,
                boardList[position - position.div(5)]
            )
            is ListAdvHolder -> holder.bind()
        }
    }

    fun initList(boardList: List<BoardData>) {
        this.boardList.run {
            clear()
            addAll(boardList)
            notifyDataSetChanged()
        }
    }

    fun moreList(boardList: List<BoardData>) {
        this.boardList.run {
            val beforePosition = size + size.div(ADV_POSITION)
            addAll(boardList)
            notifyItemRangeInserted(beforePosition, boardList.size + beforePosition)
        }
    }

    fun addBoard(board: BoardData) {
        boardList.add(0, board)
        notifyItemInserted(0)
    }

    fun updateBoard(board: BoardData) {
        val originData = boardList.find { it.bid == board.bid }
        originData?.let {
            val index = boardList.indexOf(it)
            boardList[index] = board
            val advIndex = index.div(ADV_POSITION)
            notifyItemChanged((index + advIndex))
        }
    }

    fun removeBoard(bid: String) {
        val originData = boardList.find { it.bid == bid }
        originData?.let {
            val index = boardList.indexOf(it)
            boardList.removeAt(index)
            val advIndex = index.div(ADV_POSITION)
            notifyItemRemoved((index + advIndex))
        }
    }

    fun getBoardData(bid: String) =
        boardList.find { it.bid == bid }

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