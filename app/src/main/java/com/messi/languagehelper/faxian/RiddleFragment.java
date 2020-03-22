package com.messi.languagehelper.faxian;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.messi.languagehelper.BaseFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.RespoData;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.viewmodels.TXAPIViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RiddleFragment extends BaseFragment {

    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.answer)
    TextView answer;
    private int count;
    private TwistaItem mTwistaItem;
    private TXAPIViewModel mViewModel;
    private String apiType = "riddle";

    @Override
    public void onAttach(Context context) {
        try {
            super.onAttach(context);
            mProgressbarListener = (FragmentProgressbarListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TXAPIViewModel.class);
        mViewModel.init(apiType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.essay_fragment, container, false);
        ButterKnife.bind(this, view);
        initViewModel();
        return view;
    }

    private void initViewModel() {
        count = Setings.getSharedPreferences(getContext()).getInt(KeyUtil.IsShowClickToNext,0);
        mViewModel.getTwistaItem().observe(this,(data) -> onDataChange(data));
        mViewModel.isShowProgressBar().observe(this,(isShow) -> isShowProgressBar(isShow));
    }

    private void isShowProgressBar(Boolean isShow){
        if (isShow) {
            showProgressbar();
        } else {
            hideProgressbar();
        }
    }

    private void onDataChange(RespoData<TwistaItem> data){
        LogUtil.DefalutLog("TongueTwisterFragment---onDataChange");
        if (data != null && data.getData() != null) {
            mTwistaItem = data.getData();
            if (mTwistaItem != null && !TextUtils.isEmpty(mTwistaItem.getQuest())) {
                if (question != null && answer != null) {
                    question.setText(mTwistaItem.getQuest());
                    answer.setText("轻触看谜底");
                }
            }
        } else {
            ToastUtil.diaplayMesShort(getActivity(),data.getErrStr());
        }
    }

    @OnClick({R.id.question, R.id.answer})
    public void onClick() {
        if(mTwistaItem != null){
            if (!mTwistaItem.isShowResult()) {
                if(count == 0){
                    answer.setText(mTwistaItem.getAnswer() + "\n\n\n\n\n\n" + "轻触看下一条");
                    count++;
                    Setings.saveSharedPreferences(Setings.getSharedPreferences(getContext()),
                            KeyUtil.IsShowClickToNext,1);
                }else {
                    answer.setText(mTwistaItem.getAnswer());
                }
                mTwistaItem.setShowResult(true);
            } else {
                mViewModel.loadData();
            }
        }else {
            mViewModel.loadData();
        }
    }
}
