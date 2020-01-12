package com.messi.languagehelper;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.email_layout)
    TextView email_layout;
    @BindView(R.id.app_version)
    TextView app_version;
    @BindView(R.id.img_logo)
    ImageView img_logo;
    private int clickTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setActionBarTitle(getResources().getString(R.string.title_about));
        app_version.setText(Setings.getVersionName(this));
    }

    @OnClick(R.id.email_layout)
    public void onClick() {
        Setings.contantUs(AboutActivity.this);
        AVAnalytics.onEvent(this, "about_pg_send_email");
    }


    @OnClick(R.id.img_logo)
    public void onImgClick() {
        clickTime++;
        if(clickTime > 2){
            ToastUtil.diaplayMesShort(this,"ok");
            Setings.saveSharedPreferences(Setings.getSharedPreferences(this), KeyUtil.ShowCNK,1);
        }
    }
}
