package com.like.drive.motorfeed.ui.board.detail.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.common.user.UserInfo
import com.like.drive.motorfeed.data.board.CommentData
import com.like.drive.motorfeed.data.board.CommentWrapData
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.board.ReCommentData
import com.like.drive.motorfeed.repository.board.BoardRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.board.data.CommentFragmentExtra
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class BoardDetailViewModel(private val boardRepository: BoardRepository) : BaseViewModel() {

    private val _feedData = MutableLiveData<BoardData>()
    val boardData: LiveData<BoardData> get() = _feedData

    private val _commentList = MutableLiveData<List<CommentWrapData>>()
    val commentList: LiveData<List<CommentWrapData>> get() = _commentList

    private val _photoIndex = MutableLiveData(1)
    val photoIndex: LiveData<Int> get() = _photoIndex

    val optionFeedEvent = SingleLiveEvent<BoardData>()

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
    val removeFeedEvent = SingleLiveEvent<BoardData>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val warningSelfLikeEvent = SingleLiveEvent<@StringRes Int>()

    val commentCountObserver = ObservableInt(0)
    val likeCountObserver = ObservableInt(0)
    val isLikeEnable = ObservableBoolean()

    val isProgressEvent = SingleLiveEvent<Boolean>()
    val isShimmerVisible = ObservableBoolean()
    val isRefresh = ObservableBoolean(false)

    val imgUrlClickEvent = SingleLiveEvent<String>()

    val finishEvent = SingleLiveEvent<String>()

    var loadingStatus: LoadingStatus? = null

    fun initDate(boardData: BoardData) {
        boardData.let {
            _feedData.value = it
        }
    }

    fun initDate(fid: String) {

        loadingStatus()

        viewModelScope.launch {
            boardRepository.getFeed(fid, success = { feedData, commentWrapList ->
                feedData?.run {
                    commentCountObserver.set(commentWrapList?.size ?: 0)
                    likeCountObserver.set(likeCount ?: 0)

                    _feedData.value = feedData

                    _commentList.value =
                        if (commentWrapList.isNullOrEmpty()) emptyList() else commentWrapList

                    delayTime()

                } ?: finishFeed(fid)
            }, fail = {

                errorEvent.value = R.string.not_found_data
                delayTime()

            }, isLike = {
                isLikeEnable.set(it)
            })
        }
    }

    fun removeFeedListener() {
        isProgressEvent.value = true
        _feedData.value?.let {
            viewModelScope.launch {
                boardRepository.removeFeed(it,
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
    fun addFeedComment(boardData: BoardData, comment: String?) {

        isProgressEvent.value = true

        comment?.let {
            viewModelScope.launch {
                boardRepository.addComment(boardData, it,
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
                boardRepository.addReComment(_feedData.value!!, commentData, it,
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
            boardRepository.updateComment(commentData,
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
            boardRepository.updateReComment(reCommentData,
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
            boardRepository.removeComment(commentData,
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
            boardRepository.removeReComment(reCommentData,
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

    fun showFeedOptions(boardData: BoardData) {
        optionFeedEvent.value = boardData
    }

    fun addFeedLike(fid: String, uid: String) {

        if (UserInfo.userInfo?.uid == uid) {
            warningSelfLikeEvent.value = R.string.like_self_message
            return
        }

        isLikeEnable.get().let {
            val isLike = !it

            setLikeCount(isLike) {
                viewModelScope.launch {
                    boardRepository.setLike(
                        fid,
                        isLike
                    )
                }
            }
            isLikeEnable.set(isLike)
        }

    }

    fun setDetailImgClickListener(url: String) {
        imgUrlClickEvent.value = url
    }

    private fun setLikeCount(isUp: Boolean, action: () -> Unit) {
        if (isUp) {

            likeCountObserver.set(likeCountObserver.get().plus(1))
            _feedData.value?.likeCount?.plus(1)
            action()

        } else {

            if (_feedData.value?.likeCount ?: 0 >= 0) {

                likeCountObserver.set(likeCountObserver.get().minus(1))
                _feedData.value?.likeCount?.minus(1)
                action()

            }
        }

    }

    fun setReComment(str: String?) {
        reComment.value = str
    }

    private fun delayTime() {
        viewModelScope.launch {
            delay(500)
            loadingStatus()
        }
    }

    private fun loadingStatus() {
        when (loadingStatus) {
            LoadingStatus.INIT -> {
                if (isShimmerVisible.get()) isShimmerVisible.set(false) else isShimmerVisible.set(
                    true
                )
            }
            LoadingStatus.REFRESH -> {
                if (isRefresh.get()) isRefresh.set(false) else isRefresh.set(true)
            }
            else -> Unit
        }
    }

    private fun finishFeed(fid: String) {
        errorEvent.value = R.string.not_found_data
        finishEvent.value = fid
    }

}