package com.like.drive.motorfeed.data.user

import android.os.Parcelable
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterData(
    val categoryData: CategoryData? = null,
    val motorType: MotorTypeData? = null
) : Parcelable