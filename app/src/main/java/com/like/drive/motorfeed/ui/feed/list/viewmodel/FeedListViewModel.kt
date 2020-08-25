package com.like.drive.motorfeed.ui.feed.list.viewmodel

import androidx.lifecycle.*
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import timber.log.Timber

class FeedListViewModel(private val feedRepository: FeedRepository) :BaseViewModel(){

    private val _feedList = MutableLiveData<List<FeedData>>()
    val feedList :LiveData<List<FeedData>> get() = _feedList

    val errorEvent = SingleLiveEvent<Unit>()
    val feedType = MutableLiveData<FeedTypeData>()
    val motorType = MutableLiveData<MotorTypeData>()

    val feedItemClickEvent = SingleLiveEvent<String>()



    fun getFeedList(feedTypeData: FeedTypeData?=null,motorTypeData: MotorTypeData?=null){
        viewModelScope.launch {
            _feedList.value =feedRepository.getFeedList(motorTypeData, feedTypeData).
            catch {
                errorEvent.call()
            }.single().apply {
                forEach {
                    Timber.i(it.title)
                }
            }
        }
    }

     fun feedItemClickListener(feedData: FeedData?){
        feedData?.let {
            feedItemClickEvent.postValue(it.fid)
        }
    }




    fun setFilter(feedTypeData: FeedTypeData?, motorTypeData: MotorTypeData?) {
        feedType.value = feedTypeData
        motorType.value = motorTypeData
        getFeedList(feedTypeData,motorTypeData)
    }

}