package com.like.drive.motorfeed.ui.home.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.user.UserFilter
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import org.koin.core.KoinComponent
import org.koin.core.inject

class UserFilterViewModel : BaseViewModel(), KoinComponent {

    private val userPref: UserPref by inject()
    val isFilter = ObservableBoolean()

    val setUserFilterEvent = SingleLiveEvent<UserFilter>()
    val filterClickEvent = SingleLiveEvent<UserFilter>()

    val feedType = MutableLiveData<FeedTypeData>()
    val motorType = MutableLiveData<MotorTypeData>()

    init {
        userPref.userFilter?.let {

            if (it.feedType != null || it.motorType != null) {
                setFilter(it)
            } else {
                isFilter.set(false)
            }

        } ?: isFilter.set(false)

    }

    fun setFilter(feedTypeData: FeedTypeData?, motorTypeData: MotorTypeData?) {

        val motorType: MotorTypeData? = if (motorTypeData?.brandCode == 0) {
            null
        } else {
            motorTypeData
        }

        val userFilter = UserFilter(feedTypeData, motorType)

        userPref.userFilter = userFilter

        if (feedTypeData == null && motorType == null) {
            isFilter.set(false)
        } else {
            setFilter(userFilter)

        }

    }

    fun filterClickListener() {
        filterClickEvent.value = userPref.userFilter
    }

    private fun setFilter(userFilter: UserFilter) {
        setUserFilterEvent.value = userFilter
        feedType.value = userFilter.feedType
        motorType.value = userFilter.motorType
        isFilter.set(true)
    }
}