package com.like.drive.motorfeed.ui.gallery.data

import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R

data class GalleryDirectoryData(
    var display: String? = null,    //화면에 보여질 문구
    var value: String? = null       //directory, 전체인경우 null
) {

    fun getDirectoryData(galleryData: List<GalleryItemData>?): List<GalleryDirectoryData> {

        val directoryData = mutableListOf<GalleryDirectoryData>()

        directoryData.add(GalleryDirectoryData(MotorFeedApplication.getContext().getString(R.string.all)))
        galleryData?.map { it.directory }?.distinct()?.sortedBy { it }?.map {
            directoryData.add(GalleryDirectoryData(it, it))
        }

        return directoryData
    }
}