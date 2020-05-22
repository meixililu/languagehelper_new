package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcEnDictListAdapter;
import com.messi.languagehelper.bean.TranResultRoot;
import com.messi.languagehelper.databinding.FragmentEnDicBinding;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.httpservice.RetrofitApiService;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.NullUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SignUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnDicFragment extends BaseFragment {

    private String lastSearch;
    private List<String> beans;
    private FragmentEnDicBinding binding;
    private RcEnDictListAdapter mAdapter;

    public static EnDicFragment getInstance(FragmentProgressbarListener listener) {
        EnDicFragment mMainFragment = new EnDicFragment();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (binding != null && binding.getRoot() != null) {
            ViewUtil.removeFromParentView(binding.getRoot());
            return binding.getRoot();
        }
        binding = FragmentEnDicBinding.inflate(inflater);
        init();
        return binding.getRoot();
    }

    private void init(){
        beans = new ArrayList<>();
        mAdapter = new RcEnDictListAdapter();
        binding.recentUsedLv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setFooter(new Object());
        mAdapter.setItems(beans);
        binding.recentUsedLv.setAdapter(mAdapter);
    }

    private void translateController() {
        lastSearch = Setings.q;
        showProgressbar();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String platform = SystemUtil.platform;
        String network = NetworkUtil.getNetworkType(getContext());
        String sign = SignUtil.getMd5Sign(Setings.PVideoKey, timestamp, lastSearch, platform, network);
        RetrofitApiService service = RetrofitApiService.getRetrofitApiService(Setings.TranApi,
                RetrofitApiService.class);
        Call<TranResultRoot<String>> call = service.getEnDictApi(lastSearch, network, platform, sign, timestamp);
        call.enqueue(new Callback<TranResultRoot<String>>() {
                 @Override
                 public void onResponse(Call<TranResultRoot<String>> call, Response<TranResultRoot<String>> response) {
                     LogUtil.DefalutLog("---call:"+call.request().url());
                     hideProgressbar();
                     if (response.isSuccessful()) {
                         TranResultRoot<String> mResult = response.body();
                         setData(mResult.getResult());
                         LogUtil.DefalutLog("---mResult:"+mResult.getResult().toString());
                     } else {
                         ToastUtil.diaplayMesShort(getContext(),"未找到相关结果");
                     }
                 }

                 @Override
                 public void onFailure(Call<TranResultRoot<String>> call, Throwable t) {
                     LogUtil.DefalutLog("onFailure:"+t.getMessage()+"---call:"+call.request().url());
                     hideProgressbar();
                     ToastUtil.diaplayMesShort(getContext(),"未找到相关结果");
                 }
             }
        );
    }

    private void setData(List<String> datas){
        if(NullUtil.isNotEmpty(datas)){
            beans.clear();
            beans.addAll(datas);
            beans.add(0,lastSearch);
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new FinishEvent());
        }else {
            ToastUtil.diaplayMesShort(getContext(),"未找到相关结果");
        }
    }

    public void submit() {
        translateController();
    }

}
