package com.like.drive.motorfeed.ui.board.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.repository.board.BoardRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class BoardListViewModel(private val boardRepository: BoardRepository) : BaseViewModel() {

    val feedList = SingleLiveEvent<List<BoardData>>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val feedType = MutableLiveData<CategoryData>()
    val motorType = MutableLiveData<MotorTypeData>()

    var loadingStatus: LoadingStatus? = null

    val isRefresh = ObservableBoolean(false)
    val isLoading = ObservableBoolean(false)
    val isMore = ObservableBoolean(false)
    private val _initEmpty = MutableLiveData<Boolean>()
    val initEmpty: LiveData<Boolean> get() = _initEmpty

    val feedItemClickEvent = SingleLiveEvent<String>()

    var isFirst = true
    var lastDate: Date? = null

    var isFirstLoad = true

    private var categoryData: CategoryData? = null
    private var motorTypeData: MotorTypeData? = null
    private var tagQuery: String? = null

    fun initDate(
        categoryData: CategoryData? = null,
        motorTypeData: MotorTypeData? = null,
        tagQuery: String? = null
    ) {
        isFirst = true
        lastDate = null

        loadingStatus()

        this.categoryData = categoryData
        this.motorTypeData = motorTypeData
        this.tagQuery = if (!tagQuery.isNullOrBlank()) tagQuery else null

        getFeedList()
    }

    fun moreData(date: Date? = null) {
        isFirst = false
        lastDate = date

        loadingStatus = LoadingStatus.MORE
        loadingStatus()

        getFeedList(date)
    }

    private fun getFeedList(date: Date? = Date()) {
        viewModelScope.launch {
            boardRepository.getFeedList(date ?: Date(), motorTypeData, categoryData, tagQuery)
                .catch {
                    it.message
                    /**
                     * @TODO 에러값
                     * */
                    errorEvent.call()
                    loadingStatus()
                }.collect {

                    loadingStatus()

                    if (isFirst) {
                        _initEmpty.value = it.isEmpty()
                    }
                    feedList.value = it
                    isFirstLoad = false

                }
        }
    }

    fun initUserData(uid: String) {
        isFirst = true
        lastDate = null

        loadingStatus()

        getUserList(uid = uid)
    }

    fun moreUserData(date: Date? = null, uid: String) {
        isFirst = false
        lastDate = date

        loadingStatus = LoadingStatus.MORE
        loadingStatus()

        getUserList(date, uid)
    }

    private fun getUserList(date: Date? = Date(), uid: String) {
        viewModelScope.launch {
            boardRepository.getUserFeedList(date ?: Date(), uid)
                .catch {

                    it.message
                    /**
                     * @TODO 에러값
                     * */
                    errorEvent.call()
                    loadingStatus()

                }.collect {

                    loadingStatus()
                    feedList.value = it
                    _initEmpty.value = isFirst && it.isEmpty()

                }
        }
    }

    fun getLastDate() =
        feedList.value?.lastOrNull()?.createDate == lastDate

    fun feedItemClickListener(boardData: BoardData?) {
        boardData?.let {
            feedItemClickEvent.postValue(it.bid)
        }
    }

    private fun loadingStatus() {
        when (loadingStatus) {
            LoadingStatus.INIT -> {
                if (isLoading.get()) isLoading.set(false) else isLoading.set(true)
            }
            LoadingStatus.REFRESH -> {
                if (isRefresh.get()) isRefresh.set(false) else isRefresh.set(true)
            }
            else -> {
                if (isMore.get()) isMore.set(false) else isMore.set(true)
            }
        }
    }

    fun removeFeed(boardData: BoardData) {
        viewModelScope.launch {
            boardRepository.removeUserFeed(boardData)
        }
    }

}