package com.like.drive.motorfeed.data.feed

import java.util.*

data class FeedData(
    var fid: String? = "",
    val title: String,
    val content: String,
    val brandCode: Int? = null,
    val brandName: String? = null,
    val carCode: Int? = null,
    val carName: String? = null,
    val imageUrls: List<String>? = null,
    val uid: String,
    val nick: String,
    val createDate: Date,
    val updateDate: Date
)