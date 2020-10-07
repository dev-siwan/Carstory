package com.like.drive.carstory.data.user

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(
    val uid: String? = null,
    val email: String? = null,
    var profileImgPath: String? = null,
    var intro: String? = null,
    var nickName: String? = null,
    val fcmToken: String? = null,
    val userBan: Boolean = false,
    var commentSubscribe: Boolean = true,
    val admin: Boolean = false,
    val emailSignUp: Boolean = true,
    val userBanMessage: String? = null
) : Parcelable
