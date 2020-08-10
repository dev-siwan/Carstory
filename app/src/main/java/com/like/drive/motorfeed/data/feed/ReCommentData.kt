package com.like.drive.motorfeed.data.feed

import com.like.drive.motorfeed.common.user.UserInfo
import java.util.*

data class ReCommentData(
    val uid: String? = null,
    var cid: String? = null,
    var rcId: String? = null,
    val fid: String? = null,
    val nickName: String? = null,
    val profileImg: String? = null,
    val commentStr: String? = null,
    val createDate: Date? = null,
    val updateDate: Date? = null
) {
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