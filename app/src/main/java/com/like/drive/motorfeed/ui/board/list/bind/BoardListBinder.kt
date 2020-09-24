package com.like.drive.motorfeed.ui.board.list.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.ext.htmlFormat
import com.like.drive.motorfeed.ui.base.ext.setMotorType
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import com.like.drive.motorfeed.ui.board.list.adapter.BoardListAdapter

@BindingAdapter("feedList")
fun RecyclerView.setFeedList(boardList: List<BoardData>?) {
    boardList?.let {
        (adapter as BoardListAdapter).run {
            initList(it)
        }
    }
}

@BindingAdapter("filterCategory")
fun TextView.setFilterCategory(feedTypeData: CategoryData?) {
    text = htmlFormat(
        String.format(
            context.getString(R.string.filter_feed_type_desc),
            feedTypeData?.title ?: context.getString(R.string.all)
        )
    )
}

@BindingAdapter("filterMotorType")
fun TextView.setFilterMotorType(motorTypeData: MotorTypeData?) {
    text = htmlFormat(
        String.format(
            context.getString(R.string.filter_motor_type_desc),
            motorTypeData?.setMotorType() ?: context.getString(R.string.all)
        )
    )
}