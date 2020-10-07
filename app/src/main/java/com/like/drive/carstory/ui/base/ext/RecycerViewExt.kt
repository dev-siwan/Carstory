package com.like.drive.carstory.ui.base.ext

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.carstory.R
import com.like.drive.carstory.ui.base.etc.PagingCallback

fun RecyclerView.withPaging(callback: PagingCallback, isScroll: ((Boolean) -> Unit)? = null) {
    val visibleThreshold = 3
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (callback.isRequest().not()) {

                (layoutManager as? LinearLayoutManager)?.let { layoutManager ->

                    val findLastCompletelyVisibleItemPosition =
                        layoutManager.findLastCompletelyVisibleItemPosition()

                    val lastAdapterPosition: Int = layoutManager.itemCount - 1

                    if (findLastCompletelyVisibleItemPosition >= lastAdapterPosition - visibleThreshold) {
                        callback.requestMoreList()
                    }

                }
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                isScroll?.invoke(false)
            } else {
                isScroll?.invoke(true)
            }
        }
    })
}

fun RecyclerView.dividerItemDecoration() =
    DividerItemDecoration(context, LinearLayoutManager.VERTICAL).apply {
        ContextCompat.getDrawable(context, R.drawable.divider_board_list)?.let {
            setDrawable(it)
        }
    }