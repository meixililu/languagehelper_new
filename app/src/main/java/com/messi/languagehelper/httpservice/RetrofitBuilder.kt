package com.messi.languagehelper.httpservice

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

    const val Header = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36"
    private const val VIDEO_BASE_URL = "http://api.mzxbkj.com/"
    private const val TRAN_BASE_URL = "http://zyhy.mzxbkj.com/"

    private fun getRetrofit(BASE_URL: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
    }

    val vService: KVideoRetrofitService = getKVideoRetrofitService(VIDEO_BASE_URL)

    val tService: KTranRetrofitService = getRetrofit(TRAN_BASE_URL).create(KTranRetrofitService::class.java)

    fun getKVideoRetrofitService(baseUrl: String): KVideoRetrofitService {
        return getRetrofit(baseUrl).create(KVideoRetrofitService::class.java)
    }

    fun getKTranRetrofitService(baseUrl: String): KTranRetrofitService {
        return getRetrofit(baseUrl).create(KTranRetrofitService::class.java)
    }
}