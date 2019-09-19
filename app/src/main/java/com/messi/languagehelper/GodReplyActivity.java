package com.messi.languagehelper;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.ViewModel.LeisureModel;
import com.messi.languagehelper.bean.TwistaItem;
import com.messi.languagehelper.bean.TwistaResult;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GodReplyActivity extends BaseActivity {

    @BindView(R.id.ad_layout)
    FrameLayout ad_layout;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.ad_sign)
    TextView ad_sign;
    @BindView(R.id.ad_img)
    SimpleDraweeView adImg;
    @BindView(R.id.question)
    TextView question;
    private LeisureModel mLeisureModel;
    private boolean isLoading;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.godreply_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.style5_color5);
        getSupportActionBar().setTitle(getResources().getString(R.string.leisuer_shenhuifu));
        requestData();
        mLeisureModel = new LeisureModel(this);
        mLeisureModel.setXFADID(ADUtil.MRYJYSNRLAd);
        mLeisureModel.setViews(ad_sign,adImg,xx_ad_layout,ad_layout);
        mLeisureModel.showAd();
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
                                question.setText(mTwistaItem.getTitle()+"\n\n神回复：\n\n"+mTwistaItem.getContent());
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
