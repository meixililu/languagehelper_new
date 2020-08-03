package com.messi.languagehelper.httpservice

import com.messi.languagehelper.bean.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface KTranRetrofitService {

    @GET("v1/liju")
    suspend fun getLijuApi(@Query("word") word: String, @Query("network") network: String,
                   @Query("platform") platform: String, @Query("sign") sign: String,
                   @Query("timestamp") timestamp: String): Response<TranResultRoot<List<TranLijuResult>>>

    @GET("v1/endict")
    suspend fun getEnDictApi(@Query("word") word: String, @Query("network") network: String,
                     @Query("platform") platform: String, @Query("sign") sign: String,
                     @Query("timestamp") timestamp: String): Response<TranResultRoot<List<String>>>

    @GET("v1/yue")
    suspend fun tranZhYue(@Query("word") word: String, @Query("fr") fr: String, @Query("to") to: String,
                  @Query("network") network: String, @Query("platform") platform: String,
                  @Query("sign") sign: String, @Query("timestamp") timestamp: String): Response<TranResultRoot<TranYueyuResult>>

    @GET("v1/tran")
    suspend fun tranDict(@Query("q") q: String, @Query("fr") fr: String, @Query("to") to: String,
                 @Query("network") network: String, @Query("platform") platform: String,
                 @Query("sign") sign: String, @Query("type") type: Int,
                 @Query("timestamp") timestamp: String): Response<TranResultRoot<TranDictResult>>

    @Headers("User-Agent:"+RetrofitBuilder.Header)
    @POST("ajax.php?a=fy")
    suspend fun tranByJSCB(@Query("w") word: String, @Query("f") from: String,
                           @Query("t") to: String): Response<IcibaNew>

}