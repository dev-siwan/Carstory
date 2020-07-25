package com.like.drive.motorfeed.data.user

data class UserData(
    val uid:String?=null,
    val email:String?=null,
    val profileImgUrl:String?=null,
    val intro:String?=null,
    val nickName:String?=null,
    val userBan:Boolean=false
)