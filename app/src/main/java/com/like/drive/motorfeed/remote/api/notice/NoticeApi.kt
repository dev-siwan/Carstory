package com.like.drive.motorfeed.remote.api.notice

import com.like.drive.motorfeed.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface NoticeApi {
    suspend fun getNoticeList(date: Date): Flow<List<NoticeData>>
    suspend fun setNotice(noticeData: NoticeData): Flow<Boolean>
    suspend fun removeNotice(noticeData: NoticeData): Flow<Boolean>
    suspend fun getNotice(nid: String): Flow<NoticeData?>
}