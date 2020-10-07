package com.like.drive.carstory.data.user

import android.os.Parcelable
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.board.category.data.CategoryData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterData(
    val categoryData: CategoryData? = null,
    val motorType: MotorTypeData? = null
) : Parcelable