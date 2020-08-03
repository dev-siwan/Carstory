package com.like.drive.motorfeed.data.user

data class UserData(
    val uid:String?=null,
    val email:String?=null,
    var profileImgUrl:String?=null,
    var intro:String?=null,
    var nickName:String?=null,
    val userBan:Boolean=false
)