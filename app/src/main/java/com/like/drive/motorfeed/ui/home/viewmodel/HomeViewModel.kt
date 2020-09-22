package com.like.drive.motorfeed.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.user.UserFilter
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import org.koin.core.KoinComponent

class HomeViewModel : BaseViewModel(){

    val moveSearchEvent = SingleLiveEvent<Unit>()

    val setFilterEvent = SingleLiveEvent<UserFilter>()

    val feedType = MutableLiveData<FeedTypeData>()
    val motorType = MutableLiveData<MotorTypeData>()

    fun setFilterData(feedTypeData: FeedTypeData?, motorTypeData: MotorTypeData?) {

        val motorType: MotorTypeData? = if (motorTypeData?.brandCode == 0) {
            null
        } else {
            motorTypeData
        }

        val feedType: FeedTypeData? = if (feedTypeData?.typeCode == 0) {
            null
        } else {
            feedTypeData
        }

        val userFilter = UserFilter(feedType, motorType)

        this.feedType.value = feedType
        this.motorType.value = motorType

        setFilterEvent.value = userFilter

    }

}