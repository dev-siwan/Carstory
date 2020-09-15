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
import com.like.drive.motorfeed.ui.feed.data.CommentFragmentExtra
import kotlinx.coroutines.launch
import java.util.*

class FeedDetailViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    private val _feedData = MutableLiveData<FeedData>()
    val feedData: LiveData<FeedData> get() = _feedData

    private val _commentList = MutableLiveData<List<CommentWrapData>>()
    val commentList: LiveData<List<CommentWrapData>> get() = _commentList

    private val _photoIndex = MutableLiveData(1)
    val photoIndex: LiveData<Int> get() = _photoIndex

    val optionFeedEvent = SingleLiveEvent<FeedData>()

    val comment = MutableLiveData<String>()
    val reComment = MutableLiveData<String>()

    // 코멘트 이벤트
    val addCommentEvent = SingleLiveEvent<CommentData>()
    val removeCommentEvent = SingleLiveEvent<CommentData>()
    val updateCommentEvent = SingleLiveEvent<CommentData>()
    val optionsCommentEvent = SingleLiveEvent<CommentData>()

    // 리코멘트  이벤트
    val addReCommentEvent = SingleLiveEvent<ReCommentData>()
    val removeReCommentEvent = SingleLiveEvent<ReCommentData>()
    val updateReCommentEvent = SingleLiveEvent<ReCommentData>()
    val optionsReCommentEvent = SingleLiveEvent<ReCommentData>()

    // 코멘트 다이아로그 이벤트
    val showCommentDialogEvent = SingleLiveEvent<CommentFragmentExtra>()
    val completeCommentDialogEvent = SingleLiveEvent<Unit>()

    //피드 삭제 이벤트
    val removeFeedEvent = SingleLiveEvent<FeedData>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val warningSelfLikeEvent = SingleLiveEvent<@StringRes Int>()

    val commentCountObserver = ObservableInt(0)
    val likeCountObserver = ObservableInt(0)
    val isLikeEnable = ObservableBoolean(true)

    val isProgressEvent = SingleLiveEvent<Boolean>()

    val imgUrlClickEvent = SingleLiveEvent<String>()

    val finishEvent = SingleLiveEvent<String>()

    fun initDate(feedData: FeedData) {
        feedData.let {
            _feedData.value = it
        }
    }

    fun initDate(fid: String) {
        viewModelScope.launch {
            feedRepository.getFeed(fid, success = { feedData, commentWrapList ->
                feedData?.run {
                    commentCountObserver.set(commentWrapList?.size ?: 0)
                    likeCountObserver.set(likeCount ?: 0)
                    _feedData.value = feedData

                    _commentList.value =
                        if (commentWrapList.isNullOrEmpty()) emptyList() else commentWrapList
                } ?: finishFeed(fid)
            }, fail = {
                errorEvent.value = R.string.not_found_data
            })
        }
    }

    fun removeFeedListener() {
        isProgressEvent.value = true
        _feedData.value?.let {
            viewModelScope.launch {
                feedRepository.removeFeed(it,
                    success = { removeFeedEvent.value = it },
                    fail = { setFailProcess(R.string.feed_remove_error_message) })
            }
        }
    }

    fun setPhotoIndex(index: Int) {
        _photoIndex.value = index
    }

    /**
     * comment Dialog 에서 값을 받기 위한 리스너
     */
    fun showCommentDialogListener(
        isCommentUpdate: Boolean? = false,
        commentData: CommentData? = null,
        reCommentData: ReCommentData? = null
    ) {
        showCommentDialogEvent.value = CommentFragmentExtra(
            commentUpdate = isCommentUpdate,
            commentData = commentData,
            reCommentData = reCommentData
        )
    }

    /**
     * Comment Dialog 에서 등록 클릭했을때 리스너
     */
    fun commentFragmentClickListener(
        commentFragmentExtra: CommentFragmentExtra?,
        commentStrValue: String?
    ) {
        commentFragmentExtra?.run {
            if (commentUpdate == true) {
                commentData?.let {
                    updateFeedComment(it, commentStrValue!!)
                }
                reCommentData?.let {
                    updateFeedReComment(it, commentStrValue!!)
                }
            } else {
                addReFeedComment(commentFragmentExtra.commentData!!, commentStrValue)
            }
        }
    }

    /**
     * Remote 댓글 추가
     */
    fun addFeedComment(feedData: FeedData, comment: String?) {

        isProgressEvent.value = true

        comment?.let {
            viewModelScope.launch {
                feedRepository.addComment(feedData, it,
                    success = { commentData ->
                        addCommentEvent.value = commentData
                        commentCountObserver.set(commentCountObserver.get() + 1)
                    },
                    fail = {
                        setFailProcess(R.string.comment_add_error_message)
                    }
                )
            }
        }
    }

    /**
     * Remote 대댓글 추가
     */
    private fun addReFeedComment(commentData: CommentData, reCommentValue: String?) {

        isProgressEvent.value = true

        reCommentValue?.let {
            viewModelScope.launch {
                feedRepository.addReComment(_feedData.value!!, commentData, it,
                    success = {
                        completeCommentDialogEvent.call()
                        addReCommentEvent.value = it
                    },
                    fail = {
                        setFailProcess(R.string.comment_add_error_message)
                    }
                )
            }
        }
    }

    /**
     * Remote 댓글 수정
     */
    private fun updateFeedComment(commentData: CommentData, commentValue: String) {
        isProgressEvent.value = true

        viewModelScope.launch {
            commentData.commentStr = commentValue
            commentData.updateDate = Date()
            feedRepository.updateComment(commentData,
                success = {
                    updateCommentEvent.value = commentData
                    completeCommentDialogEvent.call()
                },
                fail = { setFailProcess(R.string.comment_update_error_message) })
        }
    }

    /**
     * Remote 대댓글 수정
     */
    private fun updateFeedReComment(reCommentData: ReCommentData, commentValue: String) {
        isProgressEvent.value = true

        viewModelScope.launch {
            reCommentData.commentStr = commentValue
            reCommentData.updateDate = Date()
            feedRepository.updateReComment(reCommentData,
                success = {
                    updateReCommentEvent.value = reCommentData
                    completeCommentDialogEvent.call()
                },
                fail = { setFailProcess(R.string.comment_update_error_message) })
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
                    setFailProcess(R.string.comment_remove_error_message)
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
                    setFailProcess(R.string.comment_remove_error_message)
                })
        }
    }

    private fun setFailProcess(stringID: Int) {
        errorEvent.value = stringID
        isProgressEvent.value = false
    }

    fun falseProgress() {
        isProgressEvent.value = false
    }

    fun showCommentOptions(commentData: CommentData) {
        optionsCommentEvent.value = commentData
    }

    fun showReCommentOptions(reCommentData: ReCommentData) {
        optionsReCommentEvent.value = reCommentData
    }

    fun showFeedOptions(feedData: FeedData) {
        optionFeedEvent.value = feedData
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

    fun setDetailImgClickListener(url: String) {
        imgUrlClickEvent.value = url
    }

    private fun setLikeCount(isUp: Boolean) {
        if (isUp) {
            likeCountObserver.set(likeCountObserver.get() + 1)
            _feedData.value?.likeCount?.plus(1)
        } else {
            likeCountObserver.set(likeCountObserver.get() - 1)
            _feedData.value?.likeCount?.minus(1)
        }
    }

    fun setReComment(str: String?) {
        reComment.value = str
    }

    private fun finishFeed(fid: String) {
        errorEvent.value = R.string.not_found_data
        finishEvent.value = fid
    }

}