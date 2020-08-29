package com.like.drive.motorfeed.ui.search.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.ext.pixelToDp
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

@BindingAdapter("filterType")
fun TextView.setFilterType(feedTypeData: FeedTypeData?) {
    if (feedTypeData != null) {
        enableFilter(context.getString(R.string.feed_list_filter_type, feedTypeData.title))
    } else {
        disableFilter(context.getString(R.string.feed_list_filter_type_default))
    }
}

@BindingAdapter("filterMotor")
fun TextView.setFilterBrand(motorTypeData: MotorTypeData?) {
    if (motorTypeData != null) {
        if (motorTypeData.modelCode == 0) {
            enableFilter(context.getString(R.string.feed_list_filter_motor_brand, motorTypeData.brandName))
        } else {
            enableFilter(context.getString(
                R.string.feed_list_filter_motor_brand_model,
                motorTypeData.brandName,
                motorTypeData.modelName
            ))

        }
    } else {
        disableFilter(context.getString(R.string.feed_list_filter_motor_default))
    }
}

fun TextView.enableFilter(str: String) {
    background = context.getDrawable(R.drawable.rect_solid_black_conner_4dp)
    setTextColor(context.getColor(R.color.white_100))
    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_white_down_arrow, 0)
    compoundDrawablePadding = context.pixelToDp(4f).toInt()
    text = str
}

fun TextView.disableFilter(str: String) {
    background = context.getDrawable(R.drawable.rect_solid_light_1_conner_4dp)
    setTextColor(context.getColor(android.R.color.black))
    setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_black_down_arrow, 0)
    compoundDrawablePadding = context.pixelToDp(4f).toInt()
    text = str
}