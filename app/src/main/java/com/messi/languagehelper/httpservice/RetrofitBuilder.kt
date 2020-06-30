package com.messi.languagehelper.httpservice

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    const val VIDEO_BASE_URL = "http://api.mzxbkj.com/"
    const val TRAN_BASE_URL = "http://zyhy.mzxbkj.com/"

    private fun getVRetrofit(): Retrofit{
        return getRetrofit(VIDEO_BASE_URL)
    }

    private fun getRetrofit(BASE_URL: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .build()
    }

    val kService: KRetrofitApiService = getVRetrofit().create(KRetrofitApiService::class.java)

    val tService: RetrofitApiService = getVRetrofit().create(RetrofitApiService::class.java)
}