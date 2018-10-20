package com.messi.languagehelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.avos.avoscloud.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.costom_share_layout)
    FrameLayout costom_share_layout;
    @BindView(R.id.comments_layout)
    FrameLayout comments_layout;
    @BindView(R.id.help_layout)
    FrameLayout help_layout;
    @BindView(R.id.about_layout)
    FrameLayout about_layout;
    @BindView(R.id.invite_layout)
    FrameLayout invite_layout;
    @BindView(R.id.qrcode_layout)
    FrameLayout qrcode_layout;
    @BindView(R.id.setting_layout)
    FrameLayout setting_layout;
    @BindView(R.id.offline_dic_layout)
    FrameLayout offlineDicLayout;
    @BindView(R.id.offline_dic_unread_dot)
    ImageView offlineDicUnreadDot;
    @BindView(R.id.offline_dic_layout_line)
    ImageView offline_dic_layout_line;
    @BindView(R.id.help_layout_line)
    ImageView help_layout_line;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_more));
        mSharedPreferences = Setings.getSharedPreferences(this);
        costom_share_layout.setOnClickListener(this);
        comments_layout.setOnClickListener(this);
        help_layout.setOnClickListener(this);
        about_layout.setOnClickListener(this);
        invite_layout.setOnClickListener(this);
        qrcode_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);
        offlineDicLayout.setOnClickListener(this);
        if(!Setings.getSharedPreferences(this).getBoolean(KeyUtil.OfflineDicUnreadKey,true)){
            offlineDicUnreadDot.setVisibility(View.GONE);
        }
        initVieds();
    }

    private void initVieds(){
        if (getPackageName().equals(Setings.application_id_yys) ||
                getPackageName().equals(Setings.application_id_yys_google)) {
            offline_dic_layout_line.setVisibility(View.GONE);
            offlineDicLayout.setVisibility(View.GONE);
            help_layout.setVisibility(View.GONE);
            help_layout_line.setVisibility(View.GONE);
        } else if (getPackageName().equals(Setings.application_id_yycd)) {
            offline_dic_layout_line.setVisibility(View.GONE);
            offlineDicLayout.setVisibility(View.GONE);
            help_layout.setVisibility(View.GONE);
            help_layout_line.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_layout:
                toActivity(SettingActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_tosettingpg_btn");
                break;
            case R.id.costom_share_layout:
                toActivity(ImgShareActivity.class, null);
                break;
            case R.id.offline_dic_layout:
                toActivity(OfflineDicDownloadActivity.class, null);
                offlineDicUnreadDot.setVisibility(View.GONE);
                Setings.saveSharedPreferences(Setings.getSharedPreferences(this),
                        KeyUtil.OfflineDicUnreadKey,false);
                break;
            case R.id.comments_layout:
                comment();
                break;
            case R.id.help_layout:
                toActivity(HelpActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_tohelppg_btn");
                break;
            case R.id.about_layout:
                toActivity(AboutActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_toaboutpg_btn");
                break;
            case R.id.invite_layout:
                invite();
                break;
            case R.id.qrcode_layout:
                toActivity(QRCodeShareActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_qrcode_btn");
                break;
            default:
                break;
        }
    }

    private void comment(){
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if(getPackageName().equals(Setings.application_id_yyj) ||
                    getPackageName().equals(Setings.application_id_yyj_google)){
                intent.setData(Uri.parse("market://details?id=com.messi.learnenglish"));
            } else if (getPackageName().equals(Setings.application_id_yys) ||
                    getPackageName().equals(Setings.application_id_yys_google)) {
                intent.setData(Uri.parse("market://details?id=com.messi.cantonese.study"));
            } else if (getPackageName().equals(Setings.application_id_yycd)) {
                intent.setData(Uri.parse("market://details?id=com.messi.languagehelper.chinese"));
            } else if (getPackageName().equals(Setings.application_id_xbky)) {
                intent.setData(Uri.parse("market://details?id=com.messi.languagehelper.spoken"));
            } else {
                intent.setData(Uri.parse("market://details?id=com.messi.languagehelper"));
            }
            MoreActivity.this.startActivity(intent);
            AVAnalytics.onEvent(MoreActivity.this, "more_pg_tocommendpg_btn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void invite(){
        if(getPackageName().equals(Setings.application_id_yyj) ||
                getPackageName().equals(Setings.application_id_yyj_google)){
            ShareUtil.shareText(MoreActivity.this, MoreActivity.this.getResources().getString(R.string.invite_friends_yyj));
        }else if (getPackageName().equals(Setings.application_id_yys) ||
                getPackageName().equals(Setings.application_id_yys_google)) {
            ShareUtil.shareText(MoreActivity.this, MoreActivity.this.getResources().getString(R.string.invite_friends_yys));
        } else if (getPackageName().equals(Setings.application_id_yycd)) {
            ShareUtil.shareText(MoreActivity.this, MoreActivity.this.getResources().getString(R.string.invite_friends_yycd));
        } else if (getPackageName().equals(Setings.application_id_xbky)) {
            ShareUtil.shareText(MoreActivity.this, MoreActivity.this.getResources().getString(R.string.invite_friends_xbky));
        } else {
            ShareUtil.shareText(MoreActivity.this, MoreActivity.this.getResources().getString(R.string.invite_friends_zyhy));
        }
        AVAnalytics.onEvent(this, "more_pg_invite_btn", "邀请小伙伴", 1);
    }

}
