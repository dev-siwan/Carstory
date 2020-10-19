package com.like.drive.carstory.ui.base.ext

import java.text.DateFormat
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

fun getTimeAgo(date: Date, time: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.HOUR, -time)

    return calendar.time
}

fun getDaysAgo(days: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DAY_OF_MONTH, -days)

    return calendar.time
}

