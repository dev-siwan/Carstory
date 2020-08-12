package com.like.drive.motorfeed.data.feed

import android.os.Parcelable
import com.like.drive.motorfeed.common.user.UserInfo
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class ReCommentData(
    val uid: String? = null,
    var cid: String? = null,
    var rcId: String? = null,
    val fid: String? = null,
    val nickName: String? = null,
    val profileImg: String? = null,
    var commentStr: String? = null,
    val createDate: Date? = null,
    var updateDate: Date? = null
):Parcelable {
    fun createComment(fid: String, cid: String, commentStr: String) = ReCommentData(
        uid = UserInfo.userInfo?.uid ?: "",
        fid = fid,
        cid = cid,
        nickName = UserInfo.userInfo?.nickName ?: "",
        profileImg = UserInfo.userInfo?.profileImgUrl,
        commentStr = commentStr,
        createDate = Date(),
        updateDate = Date()
    )
}

data class ReCommentWrapData(val reCommentData: ReCommentData,val position:Int)