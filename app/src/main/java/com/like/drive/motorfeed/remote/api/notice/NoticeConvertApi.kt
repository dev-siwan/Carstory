package com.like.drive.motorfeed.remote.api.notice

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NoticeConvertApi {

    @GET("notice/{fileName}")
    suspend fun getNoticeContent(@Path("fileName") name: String): Response<String>
}