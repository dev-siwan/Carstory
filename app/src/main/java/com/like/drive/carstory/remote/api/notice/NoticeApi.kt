package com.like.drive.carstory.remote.api.notice

import com.like.drive.carstory.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface NoticeApi {
    suspend fun getNoticeList(date: Date): Flow<List<NoticeData>>
    suspend fun setNotice(noticeData: NoticeData): Flow<Boolean>
    suspend fun removeNotice(noticeData: NoticeData): Flow<Boolean>
    suspend fun getNotice(nid: String): Flow<NoticeData?>
}