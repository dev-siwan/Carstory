package com.like.drive.motorfeed.ui.feed.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.feed.*

class FeedDetailViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    private val _feedData = MutableLiveData<FeedData>()
    val feedData: LiveData<FeedData> get() = _feedData

    private val _commentList = MutableLiveData<List<CommentWrapData>>()
    val commentList: LiveData<List<CommentWrapData>> get() = _commentList

    private val _photoIndex = MutableLiveData(1)
    val photoIndex: LiveData<Int> get() = _photoIndex

    val comment = MutableLiveData<String>()
    val reComment = MutableLiveData<String>()


    val addCommentEvent = SingleLiveEvent<CommentData>()

    val showReCommentEvent = SingleLiveEvent<CommentData>()
    val reCommentEvent =SingleLiveEvent<ReCommentData>()

    val reCommentCompleteEvent = SingleLiveEvent<Unit>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val warningSelfLikeEvent = SingleLiveEvent<@StringRes Int>()

    val commentCountObserver = ObservableInt(0)
    val likeCountObserver = ObservableInt(0)
    val isLikeEnable = ObservableBoolean(true)

    fun initDate(feedData: FeedData) {
        feedData.let {
            _feedData.value = it
            _commentList.value = emptyList()
        }
    }

    fun initDate(fid: String) {
        viewModelScope.launch {
            feedRepository.getFeed(fid, success = { feedData, commentWrapList ->
                feedData?.run {
                    commentCountObserver.set(commentWrapList?.size ?: 0)
                    likeCountObserver.set(likeCount ?: 0)
                    _feedData.value = feedData
                }
                _commentList.value = if (commentWrapList.isNullOrEmpty()) emptyList() else commentWrapList
            }, fail = {

            })
        }
    }

    fun setPhotoIndex(index: Int) {
        _photoIndex.value = index
    }


    fun showReComment(commentData: CommentData){
        showReCommentEvent.value = commentData
    }

    fun addFeedComment(fid: String, comment: String?) {
        comment?.let {
            viewModelScope.launch {
                feedRepository.addComment(fid, it,
                    success = {
                        addCommentEvent.value = it
                        commentCountObserver.set(commentCountObserver.get() + 1)
                    },
                    fail = {
                        errorEvent.value = R.string.comment_error_message
                    }
                )
            }
        }
    }

    fun addReFeedComment(fid: String, cid:String, comment: String?) {
        comment?.let {
            viewModelScope.launch {
                feedRepository.addReComment(fid,cid, it,
                    success = {
                        reCommentCompleteEvent.call()
                        reCommentEvent.value = it
                    },
                    fail = {
                        errorEvent.value = R.string.comment_error_message
                    }
                )
            }
        }
    }

    fun addFeedLike(fid: String, uid: String) {

        if (UserInfo.userInfo?.uid == uid) {
            warningSelfLikeEvent.call()
            return
        }

        isLikeEnable.get().let {
            viewModelScope.launch { feedRepository.setLike(fid, it) }
            setLikeCount(it)
            isLikeEnable.set(!it)
        }
    }

    private fun setLikeCount(isUp: Boolean) {
        if (isUp) {
            likeCountObserver.set(likeCountObserver.get() + 1)
        } else {
            likeCountObserver.set(likeCountObserver.get() - 1)
        }
    }

}