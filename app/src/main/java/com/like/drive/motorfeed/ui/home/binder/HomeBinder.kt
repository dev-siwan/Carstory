package com.like.drive.motorfeed.ui.home.binder

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

@BindingAdapter("filterType")
fun TextView.setFilterType(feedTypeData: FeedTypeData?) {
    if (feedTypeData != null) {
        disableFilter(context.getString(R.string.feed_list_filter_type, feedTypeData.title))
    } else {
        disableFilter(context.getString(R.string.feed_list_filter_type_default))
    }
}

@BindingAdapter("filterMotor")
fun TextView.setFilterBrand(motorTypeData: MotorTypeData?) {
    if (motorTypeData != null) {
        if (motorTypeData.modelCode == 0) {
            disableFilter(
                context.getString(
                    R.string.feed_list_filter_motor_brand,
                    motorTypeData.brandName
                )
            )
        } else {
            disableFilter(
                context.getString(
                    R.string.feed_list_filter_motor_brand_model,
                    motorTypeData.brandName,
                    motorTypeData.modelName
                )
            )

        }
    } else {
        disableFilter(context.getString(R.string.feed_list_filter_motor_default))
    }
}

fun TextView.disableFilter(str: String) {
    background = context.getDrawable(R.drawable.rect_solid_light_1_conner_4dp)
    setTextColor(context.getColor(android.R.color.black))
    text = str
}