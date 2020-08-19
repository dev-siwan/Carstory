package com.like.drive.motorfeed.ui.feed.type.data

import android.content.Context
import android.os.Parcelable
import com.like.drive.motorfeed.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeedTypeData(
    val title: String,
    val content: String,
    val typeCode: Int
):Parcelable

fun getFeedTypeList(context:Context):List<FeedTypeData>{
    return listOf(
        FeedTypeData(
            title = context.getString(R.string.feed_type_title_1),
            content = context.getString(R.string.feed_type_content_1),
            typeCode = 1),
        FeedTypeData(
            title = context.getString(R.string.feed_type_title_2),
            content = context.getString(R.string.feed_type_content_2),
            typeCode = 2),
        FeedTypeData(
            title = context.getString(R.string.feed_type_title_3),
            content = context.getString(R.string.feed_type_content_3),
            typeCode = 3),
        FeedTypeData(
            title = context.getString(R.string.feed_type_title_4),
            content = context.getString(R.string.feed_type_content_4),
            typeCode = 4),
        FeedTypeData(
            title = context.getString(R.string.feed_type_title_5),
            content = context.getString(R.string.feed_type_content_5),
            typeCode = 5),
        FeedTypeData(
            title = context.getString(R.string.feed_type_title_6),
            content = context.getString(R.string.feed_type_content_6),
            typeCode = 6)
    )
}