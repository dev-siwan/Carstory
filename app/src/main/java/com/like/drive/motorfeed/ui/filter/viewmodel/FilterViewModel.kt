package com.like.drive.motorfeed.ui.filter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

class FilterViewModel : BaseViewModel() {

    val filterFeedTypeClickEvent = SingleLiveEvent<Unit>()
    val filterMotorTypeClickEvent = SingleLiveEvent<Unit>()

    val feedType = MutableLiveData<FeedTypeData>()
    val motorType = MutableLiveData<MotorTypeData>()

    val motorTypeEvent = SingleLiveEvent<MotorTypeData>()


    fun setFilter(feedTypeData: FeedTypeData?,motorTypeData: MotorTypeData?){
        feedType.value = feedTypeData
        motorType.value = motorTypeData
    }
}