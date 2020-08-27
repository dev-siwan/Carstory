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
import kotlinx.coroutines.launch
import java.util.*

class FeedListViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    //private val _feedList = MutableLiveData<List<FeedData>>()
    val feedList =  SingleLiveEvent<List<FeedData>>()

    val errorEvent = SingleLiveEvent<Unit>()
    val feedType = MutableLiveData<FeedTypeData>()
    val motorType = MutableLiveData<MotorTypeData>()

    val feedItemClickEvent = SingleLiveEvent<String>()

    var isFirst = true
    var lastDate: Date? = null

    private var feedTypeData: FeedTypeData? = null
    private var motorTypeData: MotorTypeData? = null
    private var tagQuery: String? = null

    fun initDate(
        feedTypeData: FeedTypeData?,
        motorTypeData: MotorTypeData?,
        tagQuery: String?
    ) {
        isFirst = true

        this.feedTypeData = feedTypeData
        this.motorTypeData = motorTypeData
        this.tagQuery = tagQuery

        getFeedList(Date())
    }

    fun moreData(date: Date) {
        isFirst = false
        lastDate = date

        getFeedList(date)
    }

    private fun getFeedList(date: Date) {
        viewModelScope.launch {
            feedRepository.getFeedList(date, motorTypeData, feedTypeData, tagQuery).catch {
                it.message
                errorEvent.call()
            }.collect {
                feedList.value = it
            }
        }
    }

    fun getLastDate(): Boolean {
        return feedList.value?.lastOrNull()?.createDate == lastDate
    }

    fun feedItemClickListener(feedData: FeedData?) {
        feedData?.let {
            feedItemClickEvent.postValue(it.fid)
        }
    }

}