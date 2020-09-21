package com.like.drive.motorfeed.remote.api.notice

import com.like.drive.motorfeed.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface NoticeApi {
    suspend fun getNoticeList(date: Date): Flow<List<NoticeData>>
    suspend fun setNotice(noticeData: NoticeData): Flow<Boolean>
    suspend fun removeNotice(noticeData: NoticeData): Flow<Boolean>
}