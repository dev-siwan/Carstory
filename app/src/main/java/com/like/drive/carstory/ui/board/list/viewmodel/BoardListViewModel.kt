package com.like.drive.carstory.ui.board.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.carstory.R
import com.like.drive.carstory.common.livedata.SingleLiveEvent
import com.like.drive.carstory.data.board.BoardData
import com.like.drive.carstory.data.motor.MotorTypeData
import com.like.drive.carstory.repository.board.BoardRepository
import com.like.drive.carstory.ui.base.BaseViewModel
import com.like.drive.carstory.ui.board.category.data.CategoryData
import com.like.drive.carstory.ui.common.data.LoadingStatus
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class BoardListViewModel(private val boardRepository: BoardRepository) : BaseViewModel() {

    private val _boardList = MutableLiveData<List<BoardData>>()
    val boardList: LiveData<List<BoardData>> get() = _boardList

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val category = MutableLiveData<CategoryData>()
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

    var isUserPage = ObservableBoolean(false)

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

        this.categoryData = categoryData
        this.motorTypeData = motorTypeData
        this.tagQuery = if (!tagQuery.isNullOrBlank()) tagQuery else null

        getBoardList()
    }

    fun moreData(date: Date? = null) {
        isFirst = false
        lastDate = date

        setLoading(LoadingStatus.MORE)

        getBoardList(date)
    }

    private fun getBoardList(date: Date? = Date()) {
        viewModelScope.launch {
            boardRepository.getBoardList(date ?: Date(), motorTypeData, categoryData, tagQuery)
                .catch {
                    it.message
                    errorEvent.value = R.string.get_list_error_message

                    hideLoading()

                }.collect {

                    if (isFirst) {
                        _initEmpty.value = it.isEmpty()
                    }
                    _boardList.value = it
                    isFirstLoad = false

                    hideLoading()

                }
        }
    }

    fun initUserData(uid: String) {
        isFirst = true
        lastDate = null
        getUserBoardList(uid = uid)
    }

    fun moreUserData(date: Date? = null, uid: String) {
        isFirst = false
        lastDate = date

        setLoading(LoadingStatus.MORE)

        getUserBoardList(date, uid)
    }

    private fun getUserBoardList(date: Date? = Date(), uid: String) {
        viewModelScope.launch {
            boardRepository.getUserBoardList(date ?: Date(), uid)
                .catch {

                    it.message
                    errorEvent.value = R.string.get_list_error_message
                    hideLoading()

                }.collect {

                    _boardList.value = it
                    _initEmpty.value = isFirst && it.isEmpty()

                    hideLoading()

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

    fun setLoading(status: LoadingStatus) {
        loadingStatus = status
        showLoading()
    }

    private fun showLoading() {
        when (loadingStatus) {
            LoadingStatus.INIT -> {
                if (!isLoading.get()) {
                    isLoading.set(true)
                }
            }
            LoadingStatus.REFRESH -> {
                if (!isRefresh.get()) {
                    isRefresh.set(true)
                }
            }
            LoadingStatus.MORE -> {
                if (!isMore.get()) {
                    isMore.set(true)
                }
            }
            else -> Unit
        }
    }

    private fun hideLoading() {
        if (isLoading.get()) {
            isLoading.set(false)
            return
        }
        if (isMore.get()) {
            isMore.set(false)
            return
        }
        if (isRefresh.get()) {
            isRefresh.set(false)
        }
    }

    fun removeBoard(boardData: BoardData) {
        viewModelScope.launch {
            boardRepository.removeEmptyBoard(boardData)
        }
    }

}