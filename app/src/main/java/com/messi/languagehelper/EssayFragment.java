package com.messi.languagehelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.EssayData;
import com.messi.languagehelper.bean.EssayRoot;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;

public class EssayFragment extends BaseFragment {

    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.answer)
    TextView answer;
    @BindView(R.id.answer_cover)
    FrameLayout answerCover;
    private EssayData mEssayData;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        requestData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.essay_fragment, container, false);
        ButterKnife.bind(this, view);
        initSwipeRefresh(view);
        return view;
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        requestData();
    }

    private void requestData() {
        showProgressbar();
        final FormBody formBody = new FormBody.Builder()
                .add("showapi_appid", Settings.showapi_appid)
                .add("showapi_sign", Settings.showapi_secret)
                .add("showapi_timestamp", String.valueOf(System.currentTimeMillis()))
                .add("showapi_res_gzip", "1")
                .add("count", "1")
                .build();
        LanguagehelperHttpClient.post(Settings.EssayApi, formBody, new UICallback(getActivity()) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    if (JsonParser.isJson(responseString)) {
                        EssayRoot mRoot = JSON.parseObject(responseString, EssayRoot.class);
                        if (mRoot != null && mRoot.getShowapi_res_code() == 0 && mRoot.getShowapi_res_body() != null) {
                            if (mRoot.getShowapi_res_body().getData() != null && mRoot.getShowapi_res_body().getData().size() > 0) {
                                mEssayData = mRoot.getShowapi_res_body().getData().get(0);
                                question.setText(mEssayData.getEnglish());
                                answer.setText("轻触看中文");
                            }

                        } else {
                            ToastUtil.diaplayMesShort(getContext(), mRoot.getShowapi_res_error());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(getContext(), EssayFragment.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
            }
        });
    }

    @OnClick(R.id.answer_cover)
    public void onClick() {
        if(mEssayData != null){
            if (!mEssayData.isShowResult()) {
                answer.setText(mEssayData.getChinese() + "\n\n\n" + "(轻触更新下一条)");
                mEssayData.setShowResult(true);
            } else {
                requestData();
            }
        }
    }
}
