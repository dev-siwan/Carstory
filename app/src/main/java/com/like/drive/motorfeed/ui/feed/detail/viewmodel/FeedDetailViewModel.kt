package com.like.drive.motorfeed.ui.feed.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FeedDetailViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    private val _feedData = MutableLiveData<FeedData>()
    val feedData: LiveData<FeedData> get() = _feedData

    private val _commentList = MutableLiveData<List<CommentData>>()
    val commentList: LiveData<List<CommentData>> get() = _commentList

    private val _photoIndex = MutableLiveData(1)
    val photoIndex : LiveData<Int> get() = _photoIndex

    fun initDate(feedData: FeedData) {
        feedData.let {
            _feedData.value = it
            _commentList.value = emptyList()
        }
    }

    fun initDate(fid: String) {
        viewModelScope.launch {
            feedRepository.getFeed(fid)
                .zip(feedRepository.getFeedComment(fid)) { feedData, commentList ->
                    _feedData.value = feedData
                    _commentList.value =
                        if (commentList.isNullOrEmpty()) emptyList() else commentList
                }.catch { }
                .collect()
        }

    }

    fun setPhotoIndex(index:Int){
        _photoIndex.value=index
    }

}