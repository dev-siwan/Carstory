package com.like.drive.carstory.util.photo

import android.database.Cursor
import android.net.Uri
import com.like.drive.carstory.CarStoryApplication

object PhotoUtil {
    fun getPathFromUri(uri: Uri?): String? {
        if (uri == null) return null
        val getCursor: Cursor? =
            CarStoryApplication.getContext().contentResolver.query(uri, null, null, null, null)
        return getCursor?.let { cursor ->
            cursor.moveToNext()
            val path = cursor.getString(cursor.getColumnIndex("_data"))
            cursor.close()
            return@let path
        }
    }

    fun getMimeType(url: String): Boolean {
        return url.toLowerCase().matches(Regex("^.*\\.jpg$")) || url.toLowerCase().matches(Regex("^.*\\.jpeg$")) || url.toLowerCase().matches(Regex("^.*\\.png$")) ||  url.toLowerCase().matches(Regex("^.*\\.gif$"))
    }
}