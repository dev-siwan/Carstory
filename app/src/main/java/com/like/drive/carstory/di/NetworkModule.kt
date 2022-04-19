package com.like.drive.carstory.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.like.drive.carstory.common.define.GitDefine
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(CustomInterceptor())
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okhttp: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okhttp)
        .baseUrl(GitDefine.GIT_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

}

class CustomInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url
        val builder = originalHttpUrl.newBuilder()
        val url = builder.build()

        val requestBuilder = original.newBuilder()
            .addHeader(GitDefine.HEADER_AUTH, GitDefine.GIT_TOKEN)
            .url(url)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}