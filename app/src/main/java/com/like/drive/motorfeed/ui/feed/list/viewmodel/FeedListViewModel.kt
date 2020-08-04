package com.like.drive.motorfeed.ui.feed.list.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FeedListViewModel(val feedRepository: FeedRepository) :BaseViewModel(){

    private val _feedList =MutableLiveData<List<FeedData>>()
    val feedList : LiveData<List<FeedData>> get() = _feedList

    val errorEvent = SingleLiveEvent<Unit>()

    init {
        getFeedList()
    }

    private fun getFeedList(){
        viewModelScope.launch {
            feedRepository.getFeedList().catch {
                errorEvent.call()
            }.collect {
                _feedList.value = it
            }
        }

    }

}