package com.like.drive.motorfeed.ui.base.binder

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.util.*

@BindingAdapter("formatTimeStr")
fun TextView.formatTimeString(date: Date?) {
    date?.let {date->
        val curTime = System.currentTimeMillis()
        var diffTime: Long = (curTime - date.time) / 1000

        text = when {
            diffTime < SEC -> {
                "방금 전"
            }
            SEC.let { diffTime /= it; diffTime } < MIN -> {
                diffTime.toString() + "분 전"
            }
            MIN.let { diffTime /= it; diffTime } < HOUR -> {
                diffTime.toString() + "시간 전"
            }
            HOUR.let { diffTime /= it; diffTime } < DAY -> {
                diffTime.toString() + "일 전"
            }
            DAY.let { diffTime /= it; diffTime } < MONTH -> {
                diffTime.toString() + "달 전"
            }
            else -> {
                diffTime.toString() + "년 전"
            }
        }
    }
}

const val SEC = 60
const val MIN = 60
const val HOUR = 24
const val DAY = 30
const val MONTH = 12