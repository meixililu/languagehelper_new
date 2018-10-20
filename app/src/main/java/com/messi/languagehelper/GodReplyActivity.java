package com.messi.languagehelper;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.bean.TwistaResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GodReplyActivity extends BaseActivity {

    @BindView(R.id.xx_layout)
    FrameLayout xxAdLayout;
    @BindView(R.id.question)
    TextView question;
    private XFYSAD mXFYSAD;
    private boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.godreply_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.style5_color5);
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_shenhuifu));
        requestData();
        mXFYSAD = new XFYSAD(this, xxAdLayout, ADUtil.MRYJYSNRLAd);
        mXFYSAD.showAD();
    }

    private void requestData() {
        isLoading = true;
        showProgressbar();
        LanguagehelperHttpClient.get(Setings.TXGodreplyApi, new UICallback(GodReplyActivity.this) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    if (JsonParser.isJson(responseString)) {
                        TwistaResult mRoot = JSON.parseObject(responseString, TwistaResult.class);
                        if (mRoot.getCode() == 200) {
                            if (mRoot.getNewslist() != null && mRoot.getNewslist().size() > 0) {
                                TwistaItem mTwistaItem = mRoot.getNewslist().get(0);
                                LogUtil.DefalutLog(mTwistaItem.getContent());
                                question.setText(mTwistaItem.getContent());
                            }
                        } else {
                            ToastUtil.diaplayMesShort(GodReplyActivity.this, mRoot.getMsg());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(GodReplyActivity.this, GodReplyActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                isLoading = false;
                hideProgressbar();
            }
        });
    }

    @OnClick(R.id.question)
    public void onViewClicked() {
        if(!isLoading){
            requestData();
        }
    }
}
