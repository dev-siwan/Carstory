package com.like.drive.carstory.repository.notice

import com.like.drive.carstory.data.notice.NoticeData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface NoticeRepository {
    suspend fun getList(date: Date): Flow<List<NoticeData>>
    suspend fun setNotice(
        noticeData: NoticeData,
        success: (NoticeData) -> Unit,
        fail: () -> Unit
    )

    suspend fun removeNotice(
        noticeData: NoticeData,
        success: (NoticeData) -> Unit,
        fail: () -> Unit
    )

    suspend fun sendNotification(noticeData: NoticeData)

    suspend fun getNotice(
        nid: String,
        success: (NoticeData) -> Unit,
        fail: () -> Unit
    )
}