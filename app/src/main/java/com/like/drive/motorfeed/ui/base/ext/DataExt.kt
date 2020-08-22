package com.like.drive.motorfeed.ui.base.ext

import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.data.motor.MotorTypeData

fun MotorTypeData.setMotorType(): String {
    return if (modelCode == 0) {
        brandName
    } else {
        String.format(
            MotorFeedApplication.getContext().getString(R.string.motorType_format_text),
            brandName,
            modelName
        )
    }
}