package com.messi.languagehelper.httpservice;

import com.messi.languagehelper.bean.TwistaResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TXApiService {

    @GET("txapi/{type}/index?key=18f7f9dbd7dfcd8ab45efdcfbc33826d")
    Call<TwistaResult> getTwistaItem(@Path("type") String type);

}
