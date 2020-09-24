package com.like.drive.motorfeed.data.user

import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.board.category.data.CategoryData

data class UserFilter(
    val feedType:CategoryData?=null,
    val motorType: MotorTypeData?=null
)