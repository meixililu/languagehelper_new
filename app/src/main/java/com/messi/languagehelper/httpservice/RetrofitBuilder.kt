package com.messi.languagehelper.httpservice

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

    const val VIDEO_BASE_URL = "http://api.mzxbkj.com/"
    const val TRAN_BASE_URL = "http://zyhy.mzxbkj.com/"

    private fun getVRetrofit(): Retrofit{
        return getRetrofit(VIDEO_BASE_URL)
    }

    private fun getRetrofit(BASE_URL: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    val kService: KRetrofitApiService = getVRetrofit().create(KRetrofitApiService::class.java)

    val tService: RetrofitApiService = getVRetrofit().create(RetrofitApiService::class.java)
}