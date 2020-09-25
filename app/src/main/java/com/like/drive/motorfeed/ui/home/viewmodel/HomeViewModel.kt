package com.like.drive.motorfeed.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.user.FilterData
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.board.category.data.CategoryData

class HomeViewModel : BaseViewModel() {

    val moveSearchEvent = SingleLiveEvent<Unit>()

    val setFilterEvent = SingleLiveEvent<FilterData>()

    val category = MutableLiveData<CategoryData>()
    val motorType = MutableLiveData<MotorTypeData>()

    fun setFilterData(feedTypeData: CategoryData?, motorTypeData: MotorTypeData?) {

        val motorType: MotorTypeData? = if (motorTypeData?.brandCode == 0) {
            null
        } else {
            motorTypeData
        }

        val feedType: CategoryData? = if (feedTypeData?.typeCode == 0) {
            null
        } else {
            feedTypeData
        }

        val userFilter = FilterData(feedType, motorType)

        this.category.value = feedType
        this.motorType.value = motorType

        setFilterEvent.value = userFilter

    }

}