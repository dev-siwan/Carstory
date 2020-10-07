package com.like.drive.carstory.ui.home.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.carstory.R
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.base.binder.setFormatHtml
import com.like.drive.carstory.ui.board.category.data.CategoryData

@BindingAdapter("filterType")
fun TextView.setFilterType(categoryData: CategoryData?) {
    text = if (categoryData != null) {
        context.getString(R.string.filter_category_type, categoryData.title)
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
fun TextView.setEmptyMessage(categoryData: CategoryData?, motorTypeData: MotorTypeData?) {

    val categoryStr =
        categoryData?.title ?: context.getString(R.string.all)
    val motorTypeStr =
        motorTypeData?.let { if (it.modelCode == 0) it.brandName else "${it.brandName} | ${it.modelName}" }
            ?: context.getString(R.string.all)

    setFormatHtml(context.getString(R.string.filter_empty_list_message, categoryStr, motorTypeStr))

}
