package com.like.drive.carstory.ui.board.upload.data

import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.board.category.data.CategoryData

data class BoardUploadField(
    val title: String,
    val content: String,
    val category: CategoryData,
    val motorTypeData: MotorTypeData? = null,
    val tagList: ArrayList<String>? = null
)