package com.like.drive.carstory.ui.board.list.bind

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.like.drive.carstory.R
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.base.ext.htmlFormat
import com.like.drive.carstory.ui.base.ext.setMotorType
import com.like.drive.carstory.ui.board.category.data.CategoryData

@BindingAdapter("filterCategory")
fun TextView.setFilterCategory(categoryData: CategoryData?) {
    text = htmlFormat(
        String.format(
            context.getString(R.string.filter_category_type_desc),
            categoryData?.title ?: context.getString(R.string.all)
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