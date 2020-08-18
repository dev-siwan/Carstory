package com.like.drive.motorfeed.ui.view.large.viewmodel

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.data.photo.PhotoData
import com.like.drive.motorfeed.ui.base.BaseViewModel

class LargeThanViewModel : BaseViewModel() {

    private val _photoDataList = MutableLiveData<List<PhotoData>>()
    val photoDataList: LiveData<List<PhotoData>> get() = _photoDataList

    val currentIndex = ObservableInt(1)

    fun init(photoDataList: List<PhotoData>) {
        _photoDataList.value = photoDataList
    }

    fun setIndex(index:Int){
        currentIndex.set(index)
    }

}