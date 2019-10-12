package com.messi.languagehelper;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.bean.TwistaResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EssayFragment extends BaseFragment {

    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.answer)
    TextView answer;
    @BindView(R.id.answer_cover)
    FrameLayout answerCover;
    TwistaItem mTwistaItem;

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
        LanguagehelperHttpClient.get(Setings.TXEssayApi, new UICallback(getActivity()) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    if (JsonParser.isJson(responseString)) {
                        TwistaResult mRoot = JSON.parseObject(responseString, TwistaResult.class);
                        if (mRoot.getCode() == 200) {
                            if (mRoot.getNewslist() != null && mRoot.getNewslist().size() > 0) {
                                mTwistaItem = mRoot.getNewslist().get(0);
                                question.setText(mTwistaItem.getEn());
                                answer.setText("轻触看中文");
                            }

                        } else {
                            ToastUtil.diaplayMesShort(getContext(), "加载失败，请重试！");
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
        if(mTwistaItem != null){
            if (!mTwistaItem.isShowResult()) {
                answer.setText(mTwistaItem.getZh() + "\n\n\n" + "(轻触更新下一条)");
                mTwistaItem.setShowResult(true);
            } else {
                requestData();
            }
        }
    }
}
