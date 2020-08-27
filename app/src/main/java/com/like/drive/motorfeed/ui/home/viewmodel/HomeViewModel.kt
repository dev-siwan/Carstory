package com.like.drive.motorfeed.ui.home.viewmodel

import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel

class HomeViewModel: BaseViewModel(){

    val moveSearchEvent = SingleLiveEvent<Unit>()
}