package com.like.drive.carstory.ui.home.data

import com.like.drive.carstory.CarStoryApplication
import com.like.drive.carstory.R

enum class HomeTab(val resId:Int) {
    NEWS_FEED(R.string.home_news_feed),
    FILTER(R.string.home_filter);

    fun getTitle():String = CarStoryApplication.getContext().getString(resId)
}