package com.like.drive.motorfeed.ui.notice.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.like.drive.motorfeed.data.notice.NoticeData
import com.like.drive.motorfeed.remote.api.notice.NoticeConvertApi
import com.like.drive.motorfeed.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.Retrofit

class NoticeDetailViewModel : BaseViewModel(), KoinComponent {

    private val retrofit: Retrofit by inject()

    private val _mdFileContent = MutableLiveData<String>()
    val mdFileContent: LiveData<String> get() = _mdFileContent

    fun getFile(data: NoticeData) {
        viewModelScope.launch {
            retrofit.create(NoticeConvertApi::class.java).getNoticeContent(data.mdFile ?: "").body()
                ?.let {
                    _mdFileContent.value = it

                }
        }
    }
}