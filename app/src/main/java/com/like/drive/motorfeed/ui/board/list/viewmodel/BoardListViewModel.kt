package com.like.drive.motorfeed.ui.board.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.R
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.board.BoardData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.repository.board.BoardRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.board.category.data.CategoryData
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class BoardListViewModel(private val boardRepository: BoardRepository) : BaseViewModel() {

    private val _boardList = MutableLiveData<List<BoardData>>()
    val boardList: LiveData<List<BoardData>> get() = _boardList

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

    fun initData(
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

        getBoardList()
    }

    fun moreData(date: Date? = null) {
        isFirst = false
        lastDate = date

        loadingStatus = LoadingStatus.MORE
        loadingStatus()

        getBoardList(date)
    }

    private fun getBoardList(date: Date? = Date()) {
        viewModelScope.launch {
            boardRepository.getBoardList(date ?: Date(), motorTypeData, categoryData, tagQuery)
                .catch {
                    it.message
                    errorEvent.value = R.string.get_list_error_message
                    loadingStatus()
                }.collect {

                    loadingStatus()

                    if (isFirst) {
                        _initEmpty.value = it.isEmpty()
                    }
                    _boardList.value = it
                    isFirstLoad = false

                }
        }
    }

    fun initUserData(uid: String) {
        isFirst = true
        lastDate = null

        loadingStatus()

        getUserBoardList(uid = uid)
    }

    fun moreUserData(date: Date? = null, uid: String) {
        isFirst = false
        lastDate = date

        loadingStatus = LoadingStatus.MORE
        loadingStatus()

        getUserBoardList(date, uid)
    }

    private fun getUserBoardList(date: Date? = Date(), uid: String) {
        viewModelScope.launch {
            boardRepository.getUserBoardList(date ?: Date(), uid)
                .catch {

                    it.message
                    errorEvent.value = R.string.get_list_error_message
                    loadingStatus()

                }.collect {

                    loadingStatus()
                    _boardList.value = it
                    _initEmpty.value = isFirst && it.isEmpty()

                }
        }
    }

    fun getLastDate() =
        boardList.value?.lastOrNull()?.createDate == lastDate

    fun boardItemClickListener(boardData: BoardData?) {
        boardData?.let {
            feedItemClickEvent.postValue(it.bid)
        }
    }

    fun initEmptyValue(isEmpty: Boolean) {
        _initEmpty.value = isEmpty
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

    fun removeBoard(boardData: BoardData) {
        viewModelScope.launch {
            boardRepository.removeEmptyBoard(boardData)
        }
    }

}