package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.messi.languagehelper.adapter.RcJuhaiListAdapter;
import com.messi.languagehelper.bean.TranLijuResult;
import com.messi.languagehelper.bean.TranResultRoot;
import com.messi.languagehelper.databinding.FragmentJuhaiBinding;
import com.messi.languagehelper.event.FinishEvent;
import com.messi.languagehelper.httpservice.RetrofitApiService;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.SignUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JuhaiFragment extends BaseFragment {

    private List<TranLijuResult> beans;
    private RcJuhaiListAdapter mAdapter;
    private String lastSearch;
    private View view;
    private FragmentJuhaiBinding binding;

    public static JuhaiFragment getInstance(FragmentProgressbarListener listener) {
        JuhaiFragment mMainFragment = new JuhaiFragment();
        mMainFragment.setmProgressbarListener(listener);
        return mMainFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        if (view != null) {
            ViewUtil.removeFromParentView(view);
            return view;
        }
        binding = FragmentJuhaiBinding.inflate(inflater);
        view = binding.getRoot();
        init();
        return view;
    }

    private void init(){
        beans = new ArrayList<TranLijuResult>();
        mAdapter = new RcJuhaiListAdapter();
        binding.recentUsedLv.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recentUsedLv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_2, R.dimen.padding_2)
                        .build());
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
        Call<TranResultRoot<List<TranLijuResult>>> call = service.getLijuApi(lastSearch, network, platform, sign, timestamp);
        call.enqueue(new Callback<TranResultRoot<List<TranLijuResult>>>() {
                 @Override
                 public void onResponse(Call<TranResultRoot<List<TranLijuResult>>> call, Response<TranResultRoot<List<TranLijuResult>>> response) {
                     hideProgressbar();
                     if (response.isSuccessful()) {
                         TranResultRoot<List<TranLijuResult>> mResult = response.body();
                         setData(mResult.getResult());
                     } else {
                         ToastUtil.diaplayMesShort(getContext(),"未找到相关例句");
                     }
                 }

                 @Override
                 public void onFailure(Call<TranResultRoot<List<TranLijuResult>>> call, Throwable t) {
                     hideProgressbar();
                     ToastUtil.diaplayMesShort(getContext(),"未找到相关例句");
                 }
             }
        );
    }

    private void setData(List<TranLijuResult> juhaiBeans){
        if(juhaiBeans != null && !juhaiBeans.isEmpty()){
            beans.clear();
            beans.addAll(juhaiBeans);
            mAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new FinishEvent());
        }else {
            ToastUtil.diaplayMesShort(getContext(),"未找到相关例句");
        }
    }

    public void submit() {
        translateController();
    }

}
