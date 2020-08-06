package com.like.drive.motorfeed.ui.feed.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.annotation.StringRes
import com.like.drive.motorfeed.R

class FeedDetailViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    private val _feedData = MutableLiveData<FeedData>()
    val feedData: LiveData<FeedData> get() = _feedData

    private val _commentList = MutableLiveData<List<CommentData>>()
    val commentList: LiveData<List<CommentData>> get() = _commentList

    private val _photoIndex = MutableLiveData(1)
    val photoIndex : LiveData<Int> get() = _photoIndex

    val comment=MutableLiveData<String>()

    val addCommentEvent =SingleLiveEvent<CommentData>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()

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
                    _commentList.value = if (commentList.isNullOrEmpty()) emptyList() else commentList
                }.catch { }
                .collect()
        }

    }

    fun setPhotoIndex(index:Int){
        _photoIndex.value=index
    }

    fun addFeedComment(fid:String,comment:String?){
        comment?.let {
            viewModelScope.launch {
                feedRepository.addComment(fid, it,
                    success = {
                        addCommentEvent.value = it
                    },
                    fail = {
                        errorEvent.value =R.string.comment_error_message
                    }
                )
            }
        }
    }

}