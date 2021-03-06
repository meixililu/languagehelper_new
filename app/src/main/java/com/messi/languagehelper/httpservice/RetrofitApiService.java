package com.messi.languagehelper.httpservice;

import com.messi.languagehelper.bean.PVideoResult;
import com.messi.languagehelper.bean.TranDictResult;
import com.messi.languagehelper.bean.TranLijuResult;
import com.messi.languagehelper.bean.TranResultRoot;
import com.messi.languagehelper.bean.TranYueyuResult;
import com.messi.languagehelper.bean.TwistaResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApiService {

    public static Retrofit getRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static <T> T getRetrofitApiService(String baseUrl, Class<T> mclass){
        Retrofit retrofit = getRetrofit(baseUrl);
        return retrofit.create(mclass);
    }

    @GET("txapi/{type}/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d")
    Call<TwistaResult> getTwistaItem(@Path("type") String type);

    @GET("v1/pvideo")
    Call<PVideoResult> getPVideoApi(@Query("url") String url , @Query("network") String network,
                                    @Query("platform") String platform , @Query("sign") String sign,
                                    @Query("timestamp") String timestamp,
                                    @Query("order") int order,
                                    @Query("vid") String vid);

    @GET("v1/liju")
    Call<TranResultRoot<List<TranLijuResult>>> getLijuApi(@Query("word") String word , @Query("network") String network,
                                                          @Query("platform") String platform , @Query("sign") String sign,
                                                          @Query("timestamp") String timestamp);

    @GET("v1/endict")
    Call<TranResultRoot<List<String>>> getEnDictApi(@Query("word") String word , @Query("network") String network,
                                                    @Query("platform") String platform , @Query("sign") String sign,
                                                    @Query("timestamp") String timestamp);

    @GET("v1/yue")
    Call<TranResultRoot<TranYueyuResult>> tranZhYue(@Query("word") String word , @Query("fr") String fr , @Query("to") String to ,
                                    @Query("network") String network, @Query("platform") String platform ,
                                    @Query("sign") String sign, @Query("timestamp") String timestamp);

    @GET("v1/tran")
    Call<TranResultRoot<TranDictResult>> tranDict(@Query("q") String q , @Query("fr") String fr , @Query("to") String to ,
                                                  @Query("network") String network, @Query("platform") String platform ,
                                                  @Query("sign") String sign, @Query("type") int type,
                                                  @Query("timestamp") String timestamp);
}
