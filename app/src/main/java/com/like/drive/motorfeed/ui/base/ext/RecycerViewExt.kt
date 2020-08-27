package com.like.drive.motorfeed.ui.base.ext

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.ui.base.etc.PagingCallback

fun RecyclerView.withPaging(callback: PagingCallback,scrollDown:((Boolean)->Unit)?=null) {
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

                    if (dy > 0) {
                        scrollDown?.invoke(false)
                    } else {
                        scrollDown?.invoke(true)
                    }
                }
            }
        }
    })
}