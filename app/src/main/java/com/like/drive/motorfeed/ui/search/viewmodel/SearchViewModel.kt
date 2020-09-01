package com.like.drive.motorfeed.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData

class SearchViewModel : BaseViewModel() {

    private val _feedType = MutableLiveData<FeedTypeData>()
    val feedType: LiveData<FeedTypeData> get() = _feedType
    private val _motorType = MutableLiveData<MotorTypeData>()
    val motorType: LiveData<MotorTypeData> get() = _motorType
    val tagValue = MutableLiveData<String>()

    val tagValueEvent = SingleLiveEvent<String>()

    val filterClickEvent = SingleLiveEvent<Unit>()

    val filterFeedTypeClickEvent =SingleLiveEvent<Unit>()
    val filterMotorTypeClickEvent =SingleLiveEvent<Unit>()

    val searchBtnClickEvent = SingleLiveEvent<Unit>()


    fun setFilter(feedTypeData: FeedTypeData?, motorTypeData: MotorTypeData?) {
        _feedType.value = feedTypeData
        _motorType.value = motorTypeData
    }

    fun setTypeFilter(feedTypeData: FeedTypeData?){
        _feedType.value =feedTypeData
    }

    val searchFeedAction: (String?) -> Unit = this::searchComplex
    private fun searchComplex(keyword: String?) {
        tagValueEvent.value = keyword
    }


    fun setMotorFilter(motorTypeData: MotorTypeData?){
        if (motorTypeData?.brandCode == 0) {
            _motorType.value = null
            return
        }
        _motorType.value = motorTypeData
    }

}