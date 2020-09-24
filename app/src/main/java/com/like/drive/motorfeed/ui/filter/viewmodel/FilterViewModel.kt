package com.like.drive.motorfeed.ui.filter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.board.category.data.CategoryData

class FilterViewModel : BaseViewModel() {

    val filterCategoryClickEvent = SingleLiveEvent<Unit>()
    val filterMotorTypeClickEvent = SingleLiveEvent<Unit>()

    val feedType = MutableLiveData<CategoryData>()
    val motorType = MutableLiveData<MotorTypeData>()

    val motorTypeEvent = SingleLiveEvent<MotorTypeData>()

    fun setFilter(feedTypeData: CategoryData?, motorTypeData: MotorTypeData?) {
        feedType.value = feedTypeData
        motorType.value = motorTypeData
    }
}