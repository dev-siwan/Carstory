package com.like.drive.motorfeed.remote.api.img

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageApi{
    suspend fun uploadFeedImage(bid: String, imgFile: File,index:Int): Flow<Uri?>
    suspend fun profileImage(uid:String, imgFile: File):Flow<Boolean>
    suspend fun deleteFeedImage(bid: String,index: Int):Flow<Boolean>
}