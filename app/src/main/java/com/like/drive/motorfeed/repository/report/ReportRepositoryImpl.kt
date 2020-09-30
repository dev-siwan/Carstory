package com.like.drive.motorfeed.repository.report

import com.like.drive.motorfeed.data.report.ReportData
import com.like.drive.motorfeed.remote.api.report.ReportApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import java.util.*

class ReportRepositoryImpl(private val reportApi: ReportApi) : ReportRepository {

    override suspend fun getReportList(date: Date): Flow<List<ReportData>> {
        return reportApi.getReportList(date)
    }

    override suspend fun removeReport(
        reportData: ReportData,
        success: () -> Unit,
        fail: () -> Unit
    ) {
        reportApi.removeReport(reportData)
            .catch { fail.invoke() }
            .collect { success.invoke() }
    }

}