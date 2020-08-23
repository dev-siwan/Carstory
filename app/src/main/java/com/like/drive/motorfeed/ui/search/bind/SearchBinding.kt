package com.like.drive.motorfeed.ui.search.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.ext.htmlFormat
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

@BindingAdapter(value = ["searchFeedType","searchMotorType"] ,requireAll = true)
fun TextView.setSearchFilterData(feedTypeData: FeedTypeData?,motorTypeData: MotorTypeData?){
    text =when{
        feedTypeData!=null && motorTypeData==null->{
            String.format(context.getString(R.string.filter_feed_type_value),feedTypeData.title)
        }
        feedTypeData==null && motorTypeData!=null->{
            if (motorTypeData.modelCode == 0) {
                String.format(
                    context.getString(R.string.filter_motor_type_brand_value),
                    motorTypeData.brandName
                )
            } else {
                String.format(
                    context.getString(R.string.filter_motor_type_full_value),
                    motorTypeData.brandName, motorTypeData.modelName
                )
            }
        }
        feedTypeData!=null && motorTypeData!=null->{
            if (motorTypeData.modelCode == 0) {
                String.format(
                    context.getString(R.string.filter_feed_type_motor_type_brand_value),
                    feedTypeData.title,motorTypeData.brandName
                )
            } else {
                String.format(
                    context.getString(R.string.filter_feed_type_motor_type_full_value),
                    feedTypeData.title,motorTypeData.brandName, motorTypeData.modelName
                )
            }
        }
        else->{
            htmlFormat(context.getString(R.string.filter_not_select))
        }
    }
}