package com.like.drive.motorfeed.remote.api.report

import com.like.drive.motorfeed.data.report.ReportData
import kotlinx.coroutines.flow.Flow
import java.util.*

interface ReportApi {

    suspend fun sendReport(reportData: ReportData): Flow<Boolean>
    suspend fun getReportList(date: Date): Flow<List<ReportData>>
    suspend fun removeReport(reportData: ReportData): Flow<Boolean>

}