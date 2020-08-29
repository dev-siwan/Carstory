package com.like.drive.motorfeed.data.feed

import android.os.Parcelable
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.user.UserData
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ReCommentData(
    var cid: String? = null,
    var rcId: String? = null,
    val fid: String? = null,
    val userInfo: UserData? = null,
    var commentStr: String? = null,
    val createDate: Date? = null,
    var updateDate: Date? = null
) : Parcelable {
    fun createComment(fid: String, cid: String, commentStr: String) = ReCommentData(
        fid = fid,
        cid = cid,
        userInfo = UserInfo.userInfo,
        commentStr = commentStr,
        createDate = Date(),
        updateDate = Date()
    )
}

data class ReCommentWrapData(val reCommentData: ReCommentData, val position: Int)