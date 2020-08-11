package com.like.drive.motorfeed.data.feed

import android.os.Parcelable
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.feed.upload.data.FeedUploadField
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class FeedData(
    var fid: String? = null,
    val title: String? = null,
    val content: String? = null,
    val feedTypeStr:String?=null,
    val feedTypeCode:Int?=null,
    val brandCode: Int? = null,
    val brandName: String? = null,
    val modelCode: Int? = null,
    val modelName: String? = null,
    val imageUrls: List<String>? = null,
    val likeCount: Int? = 0,
    val uid: String? = null,
    val nick: String? = null,
    val profileImg: String? = null,
    val createDate: Date? = null,
    val updateDate: Date? = null
) : Parcelable {
    fun createData(
        fid: String,
        feedUploadField: FeedUploadField,
        motorTypeData: MotorTypeData?,
        imgList: ArrayList<PhotoData>
    ) =
        FeedData(
            fid = fid,
            title = feedUploadField.title,
            content = feedUploadField.content,
            feedTypeStr = feedUploadField.feedType.title,
            feedTypeCode = feedUploadField.feedType.typeCode,
            brandName = motorTypeData?.brandName,
            brandCode = motorTypeData?.brandCode,
            modelCode = motorTypeData?.modelCode,
            modelName = motorTypeData?.modelName,
            imageUrls = imgList.map { it.imgUrl!! },
            uid = UserInfo.userInfo?.uid ?: "",
            nick = UserInfo.userInfo?.nickName ?: "",
            profileImg = UserInfo.userInfo?.profileImgUrl ?: "",
            createDate = Date(),
            updateDate = Date()
        )
}