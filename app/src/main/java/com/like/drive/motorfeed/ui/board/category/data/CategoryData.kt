package com.like.drive.motorfeed.ui.board.category.data

import android.content.Context
import android.os.Parcelable
import com.like.drive.motorfeed.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryData(
    val title: String,
    val content: String,
    val typeCode: Int
):Parcelable

fun getCategoryList(context:Context):List<CategoryData>{
    return listOf(
        CategoryData(
            title = context.getString(R.string.category_title_1),
            content = context.getString(R.string.category_content_1),
            typeCode = 1),
        CategoryData(
            title = context.getString(R.string.category_title_2),
            content = context.getString(R.string.category_content_2),
            typeCode = 2),
        CategoryData(
            title = context.getString(R.string.category_title_3),
            content = context.getString(R.string.category_content_3),
            typeCode = 3),
        CategoryData(
            title = context.getString(R.string.category_title_4),
            content = context.getString(R.string.category_content_4),
            typeCode = 4),
        CategoryData(
            title = context.getString(R.string.category_title_5),
            content = context.getString(R.string.category_content_5),
            typeCode = 5),
        CategoryData(
            title = context.getString(R.string.category_title_6),
            content = context.getString(R.string.category_content_6),
            typeCode = 6)
    )
}