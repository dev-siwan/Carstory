package com.like.drive.motorfeed.ui.home.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.binder.setFormatHtml
import com.like.drive.motorfeed.ui.board.category.data.CategoryData

@BindingAdapter("filterType")
fun TextView.setFilterType(feedTypeData: CategoryData?) {
    text = if (feedTypeData != null) {
        context.getString(R.string.filter_category_type, feedTypeData.title)
    } else {
        context.getString(R.string.category_filter_type_default)
    }
}

@BindingAdapter("filterMotor")
fun TextView.setFilterBrand(motorTypeData: MotorTypeData?) {
    text = if (motorTypeData != null) {
        if (motorTypeData.modelCode == 0) {

            context.getString(
                R.string.filter_motor_brand,
                motorTypeData.brandName
            )

        } else {

            context.getString(
                R.string.filter_motor_brand_model,
                motorTypeData.brandName,
                motorTypeData.modelName
            )

        }
    } else {
        context.getString(R.string.category_filter_motor_default)
    }
}

@BindingAdapter(value = ["emptyCategory", "emptyMotorType"])
fun TextView.setEmptyMessage(feedTypeData: CategoryData?, motorTypeData: MotorTypeData?) {

    val feedTypeStr =
        feedTypeData?.title ?: context.getString(R.string.all)
    val motorTypeStr =
        motorTypeData?.let { if (it.modelCode == 0) it.brandName else "${it.brandName} | ${it.modelName}" }
            ?: context.getString(R.string.all)

    setFormatHtml(context.getString(R.string.filter_empty_list_message, feedTypeStr, motorTypeStr))

}
