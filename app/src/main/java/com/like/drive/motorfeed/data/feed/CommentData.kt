package com.like.drive.motorfeed.data.feed

import android.os.Parcelable
import com.like.drive.motorfeed.common.user.UserInfo
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CommentData(
    val uid: String? = null,
    var cid:String?=null,
    val fid:String?=null,
    val nickName: String? = null,
    val profileImg: String? = null,
    val commentStr: String? = null,
    var reCommentCount:Int? = 0,
    val createDate: Date? = null,
    val updateDate: Date? = null
):Parcelable{
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


data class CommentWrapData(val commentData:CommentData,
                           var reCommentList:MutableList<ReCommentData> = mutableListOf())