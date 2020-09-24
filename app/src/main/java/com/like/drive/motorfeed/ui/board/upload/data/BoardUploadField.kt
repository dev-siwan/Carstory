package com.like.drive.motorfeed.ui.board.upload.data

import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.board.category.data.CategoryData

data class BoardUploadField(
    val title: String,
    val content: String,
    val category: CategoryData,
    val motorTypeData: MotorTypeData? = null,
    val tagList: ArrayList<String>? = null
)