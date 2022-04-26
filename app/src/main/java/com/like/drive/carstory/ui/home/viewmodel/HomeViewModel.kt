package com.like.drive.carstory.ui.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.carstory.analytics.AnalyticsEventLog
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.data.user.FilterData
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.board.category.data.CategoryData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val analyticsEventLog: AnalyticsEventLog) :
    BaseViewModel() {

    val moveSearchEvent = SingleLiveEvent<Unit>()

    val setFilterEvent = SingleLiveEvent<FilterData>()

    private val _category = MutableLiveData<CategoryData?>()
    val category: LiveData<CategoryData?> get() = _category
    private val _motorType = MutableLiveData<MotorTypeData?>()
    val motorType: LiveData<MotorTypeData?> get() = _motorType

    fun setFilterData(categoryData: CategoryData?, motorTypeData: MotorTypeData?) {

        val motorType: MotorTypeData? = if (motorTypeData?.brandCode == 0) {
            null
        } else {
            motorTypeData
        }

        val category: CategoryData? = if (categoryData?.typeCode == 0) {
            null
        } else {
            categoryData
        }

        _category.value = category
        _motorType.value = motorType

        setFilterEvent.value = FilterData(category, motorType)

        analyticsEventLog.setFilterEvent(
            motorTypeData?.brandName,
            motorTypeData?.modelName,
            categoryData?.title
        )

    }

}