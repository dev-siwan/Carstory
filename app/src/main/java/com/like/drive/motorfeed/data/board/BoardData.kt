package com.like.drive.motorfeed.data.board

import android.os.Parcelable
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.data.user.UserData
import com.like.drive.motorfeed.ui.board.upload.data.BoardUploadField
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class BoardData(
    var fid: String? = null,
    val title: String? = null,
    val content: String? = null,
    val categoryStr: String? = null,
    val categoryCode: Int? = null,
    val brandCode: Int? = null,
    val brandName: String? = null,
    val modelCode: Int? = null,
    val modelName: String? = null,
    val tagList: List<String>? = null,
    val imageUrls: List<String>? = null,
    var likeCount: Int? = 0,
    val userInfo: UserData? = null,
    val createDate: Date? = null,
    val updateDate: Date? = null
) : Parcelable {
    fun createData(
        fid: String,
        boardUploadField: BoardUploadField,
        motorTypeData: MotorTypeData?,
        boardTagList: ArrayList<String>?,
        imgList: ArrayList<PhotoData>
    ) = BoardData(
        fid = fid,
        title = boardUploadField.title,
        content = boardUploadField.content,
        categoryStr = boardUploadField.category.title,
        categoryCode = boardUploadField.category.typeCode,
        brandName = motorTypeData?.brandName,
        brandCode = motorTypeData?.brandCode,
        modelCode = motorTypeData?.modelCode,
        modelName = motorTypeData?.modelName,
        tagList = boardTagList?.toList(),
        imageUrls = imgList.map { it.imgUrl!! },
        userInfo = UserInfo.userInfo,
        createDate = Date(),
        updateDate = Date()
    )

    fun updateData(
        boardUploadField: BoardUploadField,
        motorTypeData: MotorTypeData?,
        boardTagList: ArrayList<String>?,
        boardData: BoardData
    ) = BoardData(
        fid = boardData.fid,
        title = boardUploadField.title,
        content = boardUploadField.content,
        categoryStr = boardUploadField.category.title,
        categoryCode = boardUploadField.category.typeCode,
        brandName = motorTypeData?.brandName,
        brandCode = motorTypeData?.brandCode,
        modelCode = motorTypeData?.modelCode,
        modelName = motorTypeData?.modelName,
        tagList = boardTagList?.toList(),
        imageUrls = boardData.imageUrls,
        userInfo = UserInfo.userInfo,
        createDate = boardData.createDate,
        updateDate = Date()
    )
}