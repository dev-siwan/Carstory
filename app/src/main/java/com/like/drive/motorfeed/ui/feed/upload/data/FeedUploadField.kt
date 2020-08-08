package com.like.drive.motorfeed.ui.feed.upload.data

import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeItem

data class FeedUploadField(val title:String,
                           val content:String,
                           val feedType:FeedTypeItem,
                           val motorTypeData: MotorTypeData?=null)