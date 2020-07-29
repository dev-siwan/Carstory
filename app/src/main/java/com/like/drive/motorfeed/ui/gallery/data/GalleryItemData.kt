package com.like.drive.motorfeed.ui.gallery.data

import android.net.Uri

data class GalleryItemData(
    var uri: Uri,
    var mimeType: String,
    var directory: String,
    var sizeMb: Long,
    var selected: Boolean = false,
    var index :Int = 0
)