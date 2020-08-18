package com.like.drive.motorfeed.data.photo


import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class PhotoData(
    var file: File? = null,   //카메라로 찍은 경우
    var imgUrl: String? = null    //서버에 업로드한 경우
):Parcelable