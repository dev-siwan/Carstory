package com.like.drive.motorfeed.remote.api.img

import android.net.Uri
import com.like.drive.motorfeed.common.async.ResultState
import com.like.drive.motorfeed.data.photo.PhotoData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ImageApi{
    suspend fun uploadImage(fid: String, imgFile: File): Flow<Uri?>
}