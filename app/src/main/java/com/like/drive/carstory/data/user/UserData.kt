package com.like.drive.carstory.data.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    @SerializedName("uid")
    val uid: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("profileImgPath")
    var profileImgPath: String? = null,
    @SerializedName("intro")
    var intro: String? = null,
    @SerializedName("nickName")
    var nickName: String? = null,
    @SerializedName("fcmToken")
    val fcmToken: String? = null,
    @SerializedName("userBan")
    val userBan: Boolean = false,
    @SerializedName("commentSubscribe")
    var commentSubscribe: Boolean = true,
    @SerializedName("admin")
    val admin: Boolean = false,
    @SerializedName("userBanMessage")
    val userBanMessage: String? = null
) : Parcelable
