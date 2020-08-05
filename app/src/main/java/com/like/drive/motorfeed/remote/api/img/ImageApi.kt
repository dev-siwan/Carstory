package com.like.drive.motorfeed.remote.api.img

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageApi{
    suspend fun uploadImage(fid: String, imgFile: File): Flow<Uri?>
    suspend fun profileImage(uid:String, imgFile: File):Flow<Uri?>
}