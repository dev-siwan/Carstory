package com.like.drive.motorfeed.ui.feed.tag.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.like.drive.motorfeed.common.livedata.SingleLiveEvent
import com.like.drive.motorfeed.ui.base.BaseViewModel

class FeedTagViewModel : BaseViewModel() {

    val tagText = MutableLiveData<String>()
    val addTagEvent = SingleLiveEvent<String>()
    val removeTagEvent = SingleLiveEvent<String>()

    val addTagAction: Function1<String, Unit> = this::addTagListener

    fun addTagListener(tag: String) {
        tagText.value = null
        addTagEvent.value = tag
    }

    fun removeTagListener(tag: String) {
        removeTagEvent.value = tag
    }
}