package com.like.drive.motorfeed.remote.api.img

import com.like.drive.motorfeed.common.async.ResultState
import java.io.File

interface ImageApi{
    suspend fun uploadImageList(fid:String,imgFile:List<File>): List<String?>
}