package com.like.drive.carstory.ui.filter.viewmodel

import androidx.lifecycle.MutableLiveData
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.board.category.data.CategoryData

class FilterViewModel : BaseViewModel() {

    val filterCategoryClickEvent = SingleLiveEvent<Unit>()
    val filterMotorTypeClickEvent = SingleLiveEvent<Unit>()

    val category = MutableLiveData<CategoryData>()
    val motorType = MutableLiveData<MotorTypeData>()

    val motorTypeEvent = SingleLiveEvent<MotorTypeData>()

    fun setFilter(categoryData: CategoryData?, motorTypeData: MotorTypeData?) {
        category.value = categoryData
        motorType.value = motorTypeData
    }
}