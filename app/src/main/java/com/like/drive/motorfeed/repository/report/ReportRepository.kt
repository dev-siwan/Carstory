package com.like.drive.motorfeed.repository.report

import com.like.drive.motorfeed.data.report.ReportData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ReportRepository {
    suspend fun getReportList(date: Date): Flow<List<ReportData>>
    suspend fun removeReport(reportData: ReportData, success: () -> Unit, fail: () -> Unit)
}