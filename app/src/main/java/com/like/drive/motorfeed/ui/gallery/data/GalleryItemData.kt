package com.like.drive.motorfeed.ui.gallery.data

import android.net.Uri
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt

data class GalleryItemData(
    var uri: Uri,
    var mimeType: String,
    var directory: String,
    var sizeMb: Long,
    var selected: ObservableBoolean = ObservableBoolean(false),
    var index:ObservableInt =ObservableInt(0)
)