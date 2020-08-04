package com.messi.languagehelper.httpservice

import com.messi.languagehelper.bean.*
import com.messi.languagehelper.util.TranslateHelper
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface KTranRetrofitService {

    @GET("v1/liju")
    suspend fun getLijuApi(@Query("word") word: String,
                           @Query("network") network: String,
                           @Query("platform") platform: String,
                           @Query("sign") sign: String,
                           @Query("timestamp") timestamp: String): Response<TranResultRoot<List<TranLijuResult>>>

    @GET("v1/endict")
    suspend fun getEnDictApi(@Query("word") word: String,
                             @Query("network") network: String,
                             @Query("platform") platform: String,
                             @Query("sign") sign: String,
                             @Query("timestamp") timestamp: String): Response<TranResultRoot<List<String>>>

    @GET("v1/yue")
    suspend fun tranZhYue(@Query("word") word: String,
                          @Query("fr") fr: String,
                          @Query("to") to: String,
                          @Query("network") network: String,
                          @Query("platform") platform: String,
                          @Query("sign") sign: String,
                          @Query("timestamp") timestamp: String): Response<TranResultRoot<TranYueyuResult>>

    @GET("v1/tran")
    suspend fun tranDict(@Query("q") q: String,
                         @Query("fr") fr: String,
                         @Query("to") to: String,
                         @Query("network") network: String,
                         @Query("platform") platform: String,
                         @Query("sign") sign: String,
                         @Query("type") type: Int,
                         @Query("timestamp") timestamp: String): Response<TranResultRoot<TranDictResult>>

    @Headers("User-Agent:"+RetrofitBuilder.Header)
    @POST("ajax.php?a=fy")
    suspend fun tranByJSCB(@Query("w") word: String,
                           @Query("f") from: String,
                           @Query("t") to: String): Response<IcibaNew>

    @POST("nlp_texttranslate")
    suspend fun tranByQQFYJ(@Query("app_id") app_id: String,
                            @Query("time_stamp") time_stamp: String,
                            @Query("sign") sign: String,
                            @Query("source") source: String,
                            @Query("target") target: String,
                            @Query("text") text: String,
                            @Query("nonce_str") nonce_str: String): Response<QQTranAILabRoot>

    @Headers("User-Agent:"+RetrofitBuilder.Header)
    @GET("{text}/#keyfrom=dict2.index")
    suspend fun tranByYDWeb(@Path("text") text: String): Response<ResponseBody>

    @Headers("User-Agent:"+RetrofitBuilder.Header)
    @GET("search")
    suspend fun tranByBingWeb(@Query("q") text: String): Response<ResponseBody>

    @Headers("User-Agent:"+RetrofitBuilder.Header)
    @GET("translate")
    suspend fun tranByYDApi(@Query("i") i: String,
                            @Query("doctype") doctype: String = "json",
                            @Query("keyfrom") keyfrom: String = "fanyi.web",
                            @Query("screen") screen: String = "1080x1920",
                            @Query("client") client: String = "fanyideskweb",
                            @Query("version") version: String = "2.1",
                            @Query("smartresult") smartresult: String = "dict",
                            @Query("network") network: String = "wifi"): Response<YoudaoApiBean>


}