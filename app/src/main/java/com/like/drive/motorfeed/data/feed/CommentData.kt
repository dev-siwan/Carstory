package com.like.drive.motorfeed.data.feed

import com.like.drive.motorfeed.common.user.UserInfo
import java.util.*

data class CommentData(
    val uid: String? = null,
    var cid:String?=null,
    val fid:String?=null,
    val nickName: String? = null,
    val profileImg: String? = null,
    val commentStr: String? = null,
    val createDate: Date? = null,
    val updateDate: Date? = null
){
    fun createComment(fid:String,commentStr: String)=CommentData(
        uid = UserInfo.userInfo?.uid?:"",
        fid = fid,
        nickName = UserInfo.userInfo?.nickName?:"",
        profileImg = UserInfo.userInfo?.profileImgUrl,
        commentStr = commentStr,
        createDate = Date(),
        updateDate = Date()
    )
}