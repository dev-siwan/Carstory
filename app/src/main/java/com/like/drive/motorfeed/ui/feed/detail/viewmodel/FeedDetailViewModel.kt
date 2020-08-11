package com.like.drive.motorfeed.ui.feed.detail.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.feed.CommentData
import com.like.drive.motorfeed.data.feed.CommentWrapData
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.feed.ReCommentData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class FeedDetailViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    private val _feedData = MutableLiveData<FeedData>()
    val feedData: LiveData<FeedData> get() = _feedData

    private val _commentList = MutableLiveData<List<CommentWrapData>>()
    val commentList: LiveData<List<CommentWrapData>> get() = _commentList

    private val _photoIndex = MutableLiveData(1)
    val photoIndex: LiveData<Int> get() = _photoIndex

    val comment = MutableLiveData<String>()
    val reComment = MutableLiveData<String>()


    // 코멘트 관련 이벤트
    val addCommentEvent = SingleLiveEvent<CommentData>()
    val removeCommentEvent = SingleLiveEvent<CommentData>()
    val optionsCommentEvent = SingleLiveEvent<CommentData>()

    // 리코멘트 관련 이벤트
    val showReCommentDialogEvent = SingleLiveEvent<CommentData>()
    val addReCommentEvent = SingleLiveEvent<ReCommentData>()
    val removeReCommentEvent = SingleLiveEvent<ReCommentData>()
    val reCommentCompleteEvent = SingleLiveEvent<Unit>()
    val optionsReCommentEvent = SingleLiveEvent<ReCommentData>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val warningSelfLikeEvent = SingleLiveEvent<@StringRes Int>()

    val commentCountObserver = ObservableInt(0)
    val likeCountObserver = ObservableInt(0)
    val isLikeEnable = ObservableBoolean(true)

    val isProgressEvent = SingleLiveEvent<Boolean>()

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
                _commentList.value =
                    if (commentWrapList.isNullOrEmpty()) emptyList() else commentWrapList
            }, fail = {

            })
        }
    }

    fun setPhotoIndex(index: Int) {
        _photoIndex.value = index
    }


    fun showReCommentDialog(commentData: CommentData) {
        showReCommentDialogEvent.value = commentData
    }


    /**
     * Remote 댓글 추가
     */
    fun addFeedComment(fid: String, comment: String?) {

        isProgressEvent.value = true

        comment?.let {
            viewModelScope.launch {
                feedRepository.addComment(fid, it,
                    success = {
                        addCommentEvent.value = it
                        commentCountObserver.set(commentCountObserver.get() + 1)
                    },
                    fail = {
                        setRemoteFail(R.string.comment_add_error_message)
                    }
                )
            }
        }
    }


    /**
     * Remote 대댓글 추가
     */
    fun addReFeedComment(fid: String, cid: String, comment: String?) {

        isProgressEvent.value = true

        comment?.let {
            viewModelScope.launch {
                feedRepository.addReComment(fid, cid, it,
                    success = {
                        reCommentCompleteEvent.call()
                        addReCommentEvent.value = it
                    },
                    fail = {
                        setRemoteFail(R.string.comment_add_error_message)
                    }
                )
            }
        }
    }


    /**
     * Remote 댓글 삭제
     */
    fun removeFeedComment(commentData: CommentData) {

        isProgressEvent.value = true

        viewModelScope.launch {
            feedRepository.removeComment(commentData,
                success = {
                    removeCommentEvent.value = commentData
                    commentCountObserver.set(commentCountObserver.get() - 1)
                },
                fail = {
                    setRemoteFail(R.string.comment_remove_error_message)
                })
        }
    }

    /**
     * Remote 대댓글 삭제
     */
    fun removeFeedReComment(reCommentData: ReCommentData) {

        isProgressEvent.value = true

        viewModelScope.launch {
            feedRepository.removeReComment(reCommentData,
                success = {
                    removeReCommentEvent.value = reCommentData
                },
                fail = {
                    setRemoteFail(R.string.comment_remove_error_message)
                })
        }
    }


    private fun setRemoteFail(stringID: Int) {
        errorEvent.value = stringID
        isProgressEvent.value = false
    }

    fun falseProgress() {
        isProgressEvent.value = false
    }


    fun showCommentOptions(commentData: CommentData) {
        optionsCommentEvent.value = commentData
    }

    fun reCommentOptions(reCommentData: ReCommentData) {
        optionsReCommentEvent.value = reCommentData
    }

    fun addFeedLike(fid: String, uid: String) {

        if (UserInfo.userInfo?.uid == uid) {
            warningSelfLikeEvent.value = R.string.like_self_message
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