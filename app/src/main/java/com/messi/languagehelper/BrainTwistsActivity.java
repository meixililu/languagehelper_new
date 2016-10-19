package com.messi.languagehelper;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.HttpUrl;
import com.messi.languagehelper.dao.TXNewsResult;
import com.messi.languagehelper.dao.TwistaItem;
import com.messi.languagehelper.dao.TwistaResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BrainTwistsActivity extends BaseActivity {

    @BindView(R.id.question)
    TextView question;
    @BindView(R.id.answer)
    TextView answer;
    private TwistaItem mTwistaItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brain_twists_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_twists));
        requestData();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        requestData();
    }

    private void requestData() {
        showProgressbar();
        LanguagehelperHttpClient.get(Settings.TXBrainTwistsApi, new UICallback(BrainTwistsActivity.this) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    if (JsonParser.isJson(responseString)) {
                        TwistaResult mRoot = JSON.parseObject(responseString, TwistaResult.class);
                        if (mRoot.getCode() == 200) {
                            if(mRoot.getNewslist() != null && mRoot.getNewslist().size() > 0){
                                mTwistaItem = mRoot.getNewslist().get(0);
                                question.setText(mTwistaItem.getQuest());
                                answer.setText("轻触看答案");
                            }

                        }else {
                            ToastUtil.diaplayMesShort(BrainTwistsActivity.this, mRoot.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(BrainTwistsActivity.this, BrainTwistsActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
            }
        });
    }

    @OnClick(R.id.answer)
    public void onClick() {
        if(!mTwistaItem.isShowResult()){
            answer.setText(mTwistaItem.getResult()+"\n\n\n"+"(轻触更新下一条)");
            mTwistaItem.setShowResult(true);
        }else {
            requestData();
        }
    }
}
