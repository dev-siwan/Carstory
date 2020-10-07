package com.like.drive.carstory.ui.base.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date?.convertDateToString(
    dateFormat: SimpleDateFormat = SimpleDateFormat(
        "yyyy.MM.dd HH:mm",
        Locale.KOREAN
    )
): String =
    this?.let {
        dateFormat.format(it.time)
    } ?: ""

fun getDaysAgo(daysAgo: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

    return calendar.time
}
