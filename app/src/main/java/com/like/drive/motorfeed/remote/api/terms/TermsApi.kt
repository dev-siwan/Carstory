package com.like.drive.motorfeed.remote.api.terms

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface TermsApi {
    @GET("document/terms_user.md")
    suspend fun getTermsUser(): Response<String>
    @GET("document/terms_privacy.md")
    suspend fun getTermsPrivacy(): Response<String>
}