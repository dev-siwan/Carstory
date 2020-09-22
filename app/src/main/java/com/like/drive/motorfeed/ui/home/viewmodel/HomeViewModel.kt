package com.like.drive.motorfeed.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.user.UserFilter
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : BaseViewModel(), KoinComponent {

    val moveSearchEvent = SingleLiveEvent<Unit>()
    private val userPref: UserPref by inject()

    val setFilterEvent = SingleLiveEvent<UserFilter>()
    val filterClickEvent = SingleLiveEvent<UserFilter>()

    val feedType = MutableLiveData<FeedTypeData>()
    val motorType = MutableLiveData<MotorTypeData>()

    init {

        setFilterData(userPref.userFilter)

    }

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
        setFilterData(userFilter)
    }

    fun filterClickListener() {
        filterClickEvent.value = userPref.userFilter
    }

    private fun setFilterData(userFilter: UserFilter?) {

        val userFilterValue = userFilter?.let { userFilter } ?: UserFilter(null, null)

        userPref.userFilter = userFilterValue

        setFilterEvent.value = userFilterValue

        feedType.value = userFilterValue.feedType
        motorType.value = userFilterValue.motorType

    }

}