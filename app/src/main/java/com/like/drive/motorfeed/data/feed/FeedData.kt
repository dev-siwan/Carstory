package com.like.drive.motorfeed.data.feed

import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.upload.data.FeedUploadField
import java.util.*
import kotlin.collections.ArrayList

data class FeedData(
    var fid: String? = null,
    val title: String?=null,
    val content: String?=null,
    val brandCode: Int? = null,
    val brandName: String? = null,
    val modelCode: Int? = null,
    val modelName: String? = null,
    val imageUrls: List<String>? = null,
    val uid: String?=null,
    val nick: String?=null,
    val createDate: Date?=null,
    val updateDate: Date?=null
){
    fun createData(fid:String,feedUploadField:FeedUploadField,motorTypeData:MotorTypeData?,imgList:ArrayList<PhotoData>)=
        FeedData(
            fid = fid,
            title =feedUploadField.title,
            content = feedUploadField.content,
            brandName = motorTypeData?.brandName,
            brandCode = motorTypeData?.brandCode,
            modelCode = motorTypeData?.modelCode,
            modelName = motorTypeData?.modelName,
            imageUrls = imgList.map { it.imgUrl!! },
            uid = UserInfo.userInfo?.uid ?:"",
            nick = UserInfo.userInfo?.nickName ?:"",
            createDate = Date(),
            updateDate = Date()
        )
}