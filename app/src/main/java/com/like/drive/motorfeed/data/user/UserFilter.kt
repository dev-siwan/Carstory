package com.like.drive.motorfeed.data.user

import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

data class UserFilter(
    val feedType:FeedTypeData?=null,
    val motorType: MotorTypeData?=null
)