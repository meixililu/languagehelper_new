package com.messi.languagehelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.wxapi.YYJMainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadingPreActivity extends BaseActivity {


    @BindView(R.id.privacy_policy)
    TextView privacyPolicy;
    @BindView(R.id.privacy_tv1)
    TextView privacyTv1;
    @BindView(R.id.privacy_tv2)
    TextView privacyTv2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_pre_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.white);
        changeStatusBarTextColor(true);
        init();
        initPrivacyList();
    }

    private void init(){
        try {
            String privacyStr = getString(R.string.title_privacy_policy);
            SpannableStringBuilder builder = new SpannableStringBuilder(privacyStr);
            builder.setSpan(new ClickableText("隐私政策"),
                    privacyStr.lastIndexOf("隐"),
                    privacyStr.lastIndexOf("策")+1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            builder.setSpan(new ClickableText("服务条款"),
                    privacyStr.lastIndexOf("服"),
                    privacyStr.lastIndexOf("款")+1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            privacyPolicy.setText(builder);
            privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPrivacyList(){
        String privacyList1 = getString(R.string.privacy_1);
        String privacyList2 = getString(R.string.privacy_2);
        if(getPackageName().equals(Setings.application_id_zyhy)){
            privacyList1 = getString(R.string.privacy_1);
            privacyList2 = getString(R.string.privacy_2);
        }else if(getPackageName().equals(Setings.application_id_yys)){
            privacyList1 = getString(R.string.privacy_1_yys);
            privacyList2 = getString(R.string.privacy_2_yys);
        }else if(getPackageName().equals(Setings.application_id_yyj)){
            privacyList1 = getString(R.string.privacy_1);
            privacyList2 = getString(R.string.privacy_2);
        }else if(getPackageName().equals(Setings.application_id_ywcd)){
            privacyList1 = getString(R.string.privacy_1_ywcd);
            privacyList2 = getString(R.string.privacy_2_ywcd);
        }else if(getPackageName().equals(Setings.application_id_xbky)){

        }else if(getPackageName().equals(Setings.application_id_xbtl)){

        }else if(getPackageName().equals(Setings.application_id_qmzj)){

        }else if(getPackageName().equals(Setings.application_id_zrhy)){

        }else if(getPackageName().equals(Setings.application_id_zhhy)){

        }else{

        }
        privacyTv1.setText(privacyList1);
        privacyTv2.setText(privacyList2);
    }

    @OnClick({R.id.agree, R.id.no_agree})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree:
                toNextPage();
                break;
            case R.id.no_agree:
                pleaseAgree();
                break;
        }
    }

    public void toNextPage() {
        Class mclass = WXEntryActivity.class;
        if (getPackageName().equals(Setings.application_id_yyj) ||
                getPackageName().equals(Setings.application_id_yyj_google)) {
            mclass = YYJMainActivity.class;
        }
        Intent intent = new Intent(this, mclass);
        startActivity(intent);
        this.finish();
    }

    private void pleaseAgree() {
        new XPopup.Builder(this).asConfirm(
                "",
                "抱歉，您在同意隐私政策与服务条款后方可继续使用本软件。",
                new OnConfirmListener() {
                    @Override
                    public void onConfirm() {

                    }
                }
        ).hideCancelBtn().show();
    }

    private class ClickableText extends ClickableSpan{

        private String text;

        public ClickableText(String text){
            this.text = text;
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.BLUE);
        }
        @Override
        public void onClick(@NonNull View widget) {
            if(text.equals("服务条款")){
                toPrivacyDetail(text,Setings.ServiceUrl);
            }else if(text.equals("隐私政策")){
                toPrivacyDetail(text,Setings.PrivacyUrl);
            }
        }
    }

    private void toPrivacyDetail(String title,String url){
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra(KeyUtil.URL,url);
        intent.putExtra(KeyUtil.ActionbarTitle,title);
        startActivity(intent);
    }
}
