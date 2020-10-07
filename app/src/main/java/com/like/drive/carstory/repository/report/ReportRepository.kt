package com.like.drive.carstory.repository.report

import com.like.drive.carstory.data.report.ReportData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ReportRepository {
    suspend fun getReportList(date: Date): Flow<List<ReportData>>
    suspend fun removeReport(reportData: ReportData, success: () -> Unit, fail: () -> Unit)
}