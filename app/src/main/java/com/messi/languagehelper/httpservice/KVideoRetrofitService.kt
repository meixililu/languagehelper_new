package com.messi.languagehelper.httpservice

import com.messi.languagehelper.bean.BoutiquesBean
import com.messi.languagehelper.bean.PVideoResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface KVideoRetrofitService {

    @GET("v1/pvideo")
    suspend fun getPVideoApi(@Query("url") url: String?, @Query("network") network: String?,
                     @Query("platform") platform: String?, @Query("sign") sign: String?,
                     @Query("timestamp") timestamp: String?,
                     @Query("order") order: Int,
                     @Query("vid") vid: String?): Response<PVideoResult>

    @GET("v1/searchvideo")
    suspend fun searchVideoApi(@Query("keyword") keyword: String?, @Query("network") network: String?,
                     @Query("platform") platform: String?, @Query("sign") sign: String?,
                     @Query("timestamp") timestamp: String?,
                     @Query("page") page: Int): Response<MutableList<BoutiquesBean>>

}