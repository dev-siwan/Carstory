package com.like.drive.motorfeed.data.photo


import android.net.Uri
import java.io.File

data class PhotoData(
    var file: File? = null,   //카메라로 찍은 경우
    var imgUrl: String? = null    //서버에 업로드한 경우
)