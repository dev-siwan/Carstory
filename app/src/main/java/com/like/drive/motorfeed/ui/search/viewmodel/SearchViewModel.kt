package com.like.drive.motorfeed.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.data.motor.MotorTypeData
import com.like.drive.motorfeed.pref.UserPref
import com.like.drive.motorfeed.ui.base.BaseViewModel
import com.like.drive.motorfeed.ui.feed.type.data.FeedTypeData
import com.like.drive.motorfeed.ui.search.data.RecentlyData
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.collections.ArrayList

class SearchViewModel : BaseViewModel(), KoinComponent {

    private val userPref: UserPref by inject()

    private val recentlyListPref = userPref.recentlyData

    private val _recentlyList = MutableLiveData<ArrayList<RecentlyData>>()
    val recentlyList: LiveData<ArrayList<RecentlyData>> get() = _recentlyList

    val tagValueEvent = SingleLiveEvent<String>()
    val searchBtnClickEvent = SingleLiveEvent<Unit>()

    val searchFeedAction: (String?) -> Unit = this::searchComplex
    private fun searchComplex(keyword: String?) {
        tagValueEvent.value = keyword

        recentlyListPref.apply {
            find { it.tag == keyword }?.let {
                remove(it)
            }
            add(RecentlyData(keyword, Date()))
        }

        setRecentlyData()
    }

    init {
        setRecentlyData()
    }

    private fun  setRecentlyData() {
        recentlyListPref.run {
            sortByDescending {
                it.createDate
            }
            _recentlyList.value = this
        }
    }

    fun removeRecentlyData(recentlyData: RecentlyData){
        recentlyListPref.remove(recentlyData)
        setRecentlyData()
    }

}