package com.like.drive.motorfeed.ui.upload.data

import com.like.drive.motorfeed.data.motor.MotorTypeData

data class FeedUploadField(val title:String,
                           val content:String,
                           val motorTypeData: MotorTypeData?=null)