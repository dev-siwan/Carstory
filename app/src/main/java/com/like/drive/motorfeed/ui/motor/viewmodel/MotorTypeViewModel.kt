package com.like.drive.motorfeed.ui.motor.viewmodel

import android.text.Editable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.repository.motor.MotorTypeRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MotorTypeViewModel(private val motorTypeRepository: MotorTypeRepository) :BaseViewModel(){

    private val _motorTypeList = MutableLiveData<List<MotorTypeData>>()
    val motorTypeList :LiveData<List<MotorTypeData>> get() = _motorTypeList

    private val _motorTypeListBrand = MutableLiveData<List<MotorTypeData>>()
    val motorTypeListBrand :LiveData<List<MotorTypeData>> get() = _motorTypeListBrand

    val isSearchEmpty = ObservableBoolean()
    var brandList = ArrayList<MotorTypeData>()

    var clearSearch :Boolean = true

    val motorTypeDataCallbackEvent = SingleLiveEvent<MotorTypeData>()


    init {
        getMotorTypeData()
    }

     fun getMotorTypeData(){
        viewModelScope.launch {
            setMotorTypeListValue(motorTypeRepository.getMotorTypeList())
        }
    }

    fun searchMotorType(keyword: Editable?) {
        if(clearSearch) {
            keyword?.let {
                viewModelScope.launch {
                    setMotorTypeListValue(motorTypeRepository.searchMotorTypeList(it.toString()))
                }
            }
        }
    }

    private fun setMotorTypeListValue(list:List<MotorTypeData>){
        isSearchEmpty.set(list.isNullOrEmpty())
        _motorTypeList.value =list
    }

    fun setMotorBrandListValue() {
        viewModelScope.launch {
            if (brandList.isEmpty()) {
                brandList.add(MotorTypeData("전체"))
                brandList.addAll(
                    motorTypeRepository.getMotorTypeList().distinctBy { motorTypeData -> motorTypeData.brandCode })
            }
            _motorTypeListBrand.value = brandList
        }
    }

    fun getBrandByList(brandCode: Int) {
        clearSearch=false
        viewModelScope.launch {
            setMotorTypeListValue(motorTypeRepository.brandCodeByList(brandCode))
            delay(100)
            clearSearch=true
        }
    }

    fun setData(data:MotorTypeData){
        motorTypeDataCallbackEvent.value = data
    }

}