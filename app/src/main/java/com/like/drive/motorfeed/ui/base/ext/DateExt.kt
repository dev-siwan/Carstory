package com.like.drive.motorfeed.ui.base.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date?.convertDateToString(dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd HH:mm",Locale.KOREAN)): String =
    this?.let {
        dateFormat.format(it.time)
    } ?: ""