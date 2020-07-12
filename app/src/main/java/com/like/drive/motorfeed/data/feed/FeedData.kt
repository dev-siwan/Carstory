package com.like.drive.motorfeed.data.feed

import java.util.*

data class FeedData(
    val title: String,
    val content: String,
    val brandCode: Int?=null,
    val brandName: String?=null,
    val carCode : Int?=null,
    val carName : String?=null,
    val tag: List<String>,
    val imageUrls:List<String>,
    val userID :String,
    val uid:String,
    val nick:String,
    val createDate : Date,
    val updateDate : Date
)