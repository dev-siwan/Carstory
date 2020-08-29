package com.like.drive.motorfeed.data.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    val uid:String?=null,
    val email:String?=null,
    var profileImgUrl:String?=null,
    var intro:String?=null,
    var nickName:String?=null,
    val fcmToken:String?=null,
    val userBan:Boolean=false
):Parcelable