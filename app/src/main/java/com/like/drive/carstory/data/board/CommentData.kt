package com.like.drive.carstory.data.board

import android.os.Parcelable
import androidx.annotation.Keep
import com.like.drive.carstory.common.user.UserInfo
import com.like.drive.carstory.data.user.UserData
import kotlinx.android.parcel.Parcelize
import java.util.*

@Keep
@Parcelize
data class CommentData(
    var cid: String? = null,
    val bid: String? = null,
    val userInfo: UserData? = null,
    var commentStr: String? = null,
    var reCommentCount: Int? = 0,
    val createDate: Date? = null,
    var updateDate: Date? = null
) : Parcelable {
    fun createComment(bid: String, commentStr: String) = CommentData(
        bid = bid,
        userInfo = UserInfo.userInfo,
        commentStr = commentStr,
        createDate = Date(),
        updateDate = Date()
    )
}

@Keep
@Parcelize
data class ReCommentData(
    var cid: String? = null,
    var rcId: String? = null,
    val bid: String? = null,
    val userInfo: UserData? = null,
    var commentStr: String? = null,
    val createDate: Date? = null,
    var updateDate: Date? = null
) : Parcelable {
    fun createComment(bid: String, cid: String, commentStr: String) = ReCommentData(
        bid = bid,
        cid = cid,
        userInfo = UserInfo.userInfo,
        commentStr = commentStr,
        createDate = Date(),
        updateDate = Date()
    )
}

data class CommentWrapData(
    var commentData: CommentData,
    var reCommentList: MutableList<ReCommentData> = mutableListOf()
)