package com.like.drive.motorfeed.common.interceptor

import com.like.drive.motorfeed.common.define.GitDefine
import okhttp3.Interceptor
import okhttp3.Response

class GitHubInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val requestBuilder = original.newBuilder()
            .header(GitDefine.HEADER_AUTH, GitDefine.GIT_TOKEN)
            .build()

        return chain.proceed(requestBuilder)
    }
}