package com.like.drive.carstory.remote.api.img

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageApi{
    suspend fun uploadBoardImage(bid: String, imgFile: File, index:Int): Flow<Uri?>
    suspend fun profileImage(uid:String, imgFile: File):Flow<String>
    suspend fun removeProfileImg(path:String):Flow<Boolean>
    suspend fun deleteBoardImage(bid: String, index: Int):Flow<Boolean>
}