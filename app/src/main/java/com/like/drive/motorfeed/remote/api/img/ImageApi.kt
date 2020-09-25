package com.like.drive.motorfeed.remote.api.img

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageApi{
    suspend fun uploadFeedImage(fid: String, imgFile: File,index:Int): Flow<Uri?>
    suspend fun profileImage(uid:String, imgFile: File):Flow<Boolean>
    suspend fun deleteFeedImage(fid: String,index: Int):Flow<Boolean>
}