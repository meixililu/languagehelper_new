package com.messi.languagehelper.repositories;

import android.arch.lifecycle.MutableLiveData;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.bean.TwistaResult;
import com.messi.languagehelper.httpservice.TXApiService;
import com.messi.languagehelper.util.ContextUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwistaItemRepository {

    private static TwistaItemRepository instance;

    private String apiType;

    public static TwistaItemRepository getInstance(String apiType){
        if (instance == null) {
            instance = new TwistaItemRepository();
        }
        return instance;
    }

    public void getTwistaItem(MutableLiveData<RespoData<TwistaItem>> mItem, MutableLiveData<Boolean> loading){
        try {
            if (!loading.getValue()) {
                loadData(mItem, loading);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData(MutableLiveData<RespoData<TwistaItem>> mItem, MutableLiveData<Boolean> loading) throws Exception{
        loading.setValue(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Setings.TXBaseApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TXApiService service = retrofit.create(TXApiService.class);
        Call<TwistaResult> call = service.getTwistaItem(apiType);
        call.enqueue(new Callback<TwistaResult>() {
            @Override
            public void onResponse(Call<TwistaResult> call, Response<TwistaResult> response) {
                LogUtil.DefalutLog("loadData:"+response);
                loading.setValue(false);
                TwistaResult mRoot = response.body();
                if (mRoot != null && mRoot.getCode() == 200) {
                    if (mRoot.getNewslist() != null && mRoot.getNewslist().size() > 0) {
                        mItem.postValue(new RespoData(mRoot.getNewslist().get(0)));
                    }
                }else {
                    mItem.postValue(new RespoData(mRoot.getMsg()));
                }
            }

            @Override
            public void onFailure(Call<TwistaResult> call, Throwable t) {
                loading.setValue(false);
                mItem.postValue(new RespoData(ContextUtil.get().getContext().getResources().getString(R.string.network_error)));
            }
        });
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }

}
