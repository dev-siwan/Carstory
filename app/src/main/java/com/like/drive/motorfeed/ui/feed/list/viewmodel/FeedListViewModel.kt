package com.like.drive.motorfeed.ui.feed.list.viewmodel

import androidx.annotation.StringRes
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.feed.FeedData
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.repository.feed.FeedRepository
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.common.data.LoadingStatus
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class FeedListViewModel(private val feedRepository: FeedRepository) : BaseViewModel() {

    val feedList = SingleLiveEvent<List<FeedData>>()

    val errorEvent = SingleLiveEvent<@StringRes Int>()
    val feedType = MutableLiveData<FeedTypeData>()
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

    private var feedTypeData: FeedTypeData? = null
    private var motorTypeData: MotorTypeData? = null
    private var tagQuery: String? = null

    fun initDate(
        feedTypeData: FeedTypeData? = null,
        motorTypeData: MotorTypeData? = null,
        tagQuery: String? = null
    ) {
        isFirst = true
        lastDate = null

        loadingStatus()

        this.feedTypeData = feedTypeData
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
            feedRepository.getFeedList(date ?: Date(), motorTypeData, feedTypeData, tagQuery)
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
            feedRepository.getUserFeedList(date ?: Date(), uid)
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

    fun feedItemClickListener(feedData: FeedData?) {
        feedData?.let {
            feedItemClickEvent.postValue(it.fid)
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

    fun removeFeed(feedData: FeedData) {
        viewModelScope.launch {
            feedRepository.removeUserFeed(feedData)
        }
    }

}