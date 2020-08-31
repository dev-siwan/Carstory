package com.like.drive.motorfeed.ui.home.data

import com.like.drive.motorfeed.MotorFeedApplication
import com.like.drive.motorfeed.R

enum class HomeRoomTab(val resId:Int) {
    NEWS_FEED(R.string.home_news_feed),
    POPULAR(R.string.home_popular);

    fun getTitle():String = MotorFeedApplication.getContext().getString(resId)
}