package com.like.drive.motorfeed.ui.home.viewmodel

import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel

class NewsFeedViewModel : BaseViewModel() {
    val registerClickEvent = SingleLiveEvent<Unit>()
}