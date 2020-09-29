package com.like.drive.motorfeed.remote.api.report

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.like.drive.motorfeed.data.report.ReportData
import com.like.drive.motorfeed.remote.api.notice.NoticeApiImpl
import com.like.drive.motorfeed.remote.common.FireBaseTask
import com.like.drive.motorfeed.remote.reference.CollectionName
import kotlinx.coroutines.flow.Flow
import java.util.*

class ReportApiImpl(
    private val fireBaseTask: FireBaseTask,
    private val firestore: FirebaseFirestore
) : ReportApi {
    override suspend fun sendReport(reportData: ReportData): Flow<Boolean> {
        val ref = firestore.collection(CollectionName.REPORT)

        val rid = ref.document().id
        reportData.rid = rid

        return fireBaseTask.setData(ref.document(rid), reportData)
    }

    override suspend fun getReportList(date: Date): Flow<List<ReportData>> {

        val ref = firestore.collection(CollectionName.REPORT)
            .whereLessThan(NoticeApiImpl.CREATE_DATE_FIELD, date)
            .orderBy(NoticeApiImpl.CREATE_DATE_FIELD, Query.Direction.DESCENDING)
            .limit(NoticeApiImpl.INIT_SIZE.toLong())

        return fireBaseTask.getData(ref, ReportData::class.java)
    }

    override suspend fun removeReport(reportData: ReportData): Flow<Boolean> {

        val ref = firestore.collection(CollectionName.REPORT)

        return fireBaseTask.delete(ref.document(reportData.rid ?: ""))
    }

}