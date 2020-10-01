package com.like.drive.motorfeed.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.data.user.FilterData
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.board.category.data.CategoryData

class HomeViewModel : BaseViewModel() {

    val moveSearchEvent = SingleLiveEvent<Unit>()

    val setFilterEvent = SingleLiveEvent<FilterData>()

    private val _category = MutableLiveData<CategoryData>()
    val category: LiveData<CategoryData> get() = _category
    private val _motorType = MutableLiveData<MotorTypeData>()
    val motorType: LiveData<MotorTypeData> get() = _motorType

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

        _category.value = feedType
        _motorType.value = motorType

        setFilterEvent.value = userFilter

    }


}