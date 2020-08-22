package com.like.drive.motorfeed.ui.feed.list.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.ext.htmlFormat
import com.like.drive.motorfeed.ui.base.ext.setMotorType
import com.like.drive.motorfeed.ui.feed.list.adapter.FeedListAdapter
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

@BindingAdapter("feedList")
fun RecyclerView.setFeedList(feedList:List<FeedData>?){
    feedList?.let {
        (adapter as FeedListAdapter).run {
            initList(it)
        }
    }
}

@BindingAdapter("filterFeedType")
fun TextView.setFilterFeedType(feedTypeData: FeedTypeData?){
    text =htmlFormat(String.format(context.getString(R.string.filter_feed_type_desc),feedTypeData?.title?:context.getString(R.string.not_select)))
}

@BindingAdapter("filterMotorType")
fun TextView.setFilterMotorType(motorTypeData: MotorTypeData?){
    text =htmlFormat(String.format(context.getString(R.string.filter_motor_type_desc),motorTypeData?.setMotorType() ?:context.getString(R.string.not_select)))
}