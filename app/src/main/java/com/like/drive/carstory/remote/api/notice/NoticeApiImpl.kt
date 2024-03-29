package com.like.drive.carstory.remote.api.notice

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.like.drive.carstory.data.notice.NoticeData
import com.like.drive.carstory.remote.common.FireBaseTask
import com.like.drive.carstory.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow
import java.util.*

class NoticeApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val fireStore: FirebaseFirestore
) : NoticeApi {
    override suspend fun getNoticeList(date: Date): Flow<List<NoticeData>> {

        val ref = fireStore.collection(CollectionName.NOTICE)
            .whereLessThan(CREATE_DATE_FIELD, date)
            .orderBy(CREATE_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(INIT_SIZE.toLong())

        return fireBaseTask.getData(ref, NoticeData::class.java)

    }

    override suspend fun setNotice(noticeData: NoticeData): Flow<Boolean> {
        val ref = fireStore.collection(CollectionName.NOTICE)

        val nid = noticeData.nid ?: ref.document().id

        noticeData.nid = nid

        return fireBaseTask.setData(ref.document(nid), noticeData)
    }

    override suspend fun removeNotice(noticeData: NoticeData): Flow<Boolean> {
        return fireBaseTask.delete(
            fireStore.collection(CollectionName.NOTICE).document(noticeData.nid ?: "")
        )
    }

    override suspend fun getNotice(nid: String): Flow<NoticeData?> {
        val ref = fireStore.collection(CollectionName.NOTICE).document(nid)

        return fireBaseTask.getData(ref, NoticeData::class.java)
    }

    companion object {
        const val CREATE_DATE_FIELD = "createDate"
        const val INIT_SIZE = 5
    }
}