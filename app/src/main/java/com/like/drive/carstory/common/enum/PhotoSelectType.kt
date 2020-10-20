package com.like.drive.carstory.common.enum

import com.like.drive.carstory.R


enum class PhotoSelectType(val resID:Int) {
    CAMERA(R.string.camera_text),
    ALBUM(R.string.gallery_text)
}


enum class ProfilePhotoSelectType(val resID:Int) {
    CAMERA(R.string.camera_text),
    ALBUM(R.string.gallery_text),
    BASIC(R.string.basic_photo_text)
}