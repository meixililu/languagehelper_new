package com.messi.languagehelper;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.event.CaricatureEventAddBookshelf;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.mindorks.nybus.NYBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaricatureDetailActivity extends BaseActivity {


    @BindView(R.id.item_img)
    SimpleDraweeView itemImg;
    @BindView(R.id.item_img_bg)
    SimpleDraweeView item_img_bg;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tags)
    TextView tags;
    @BindView(R.id.author)
    TextView author;
    @BindView(R.id.source)
    TextView source;
    @BindView(R.id.views)
    TextView views;
    @BindView(R.id.des)
    TextView des;
    @BindView(R.id.add_bookshelf)
    TextView addBookshelf;
    @BindView(R.id.to_read)
    TextView toRead;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.back_btn)
    ImageView backBtn;

    private AVObject mAVObject;
    private XFYSAD mXFYSAD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transparentStatusbar();
        setContentView(R.layout.caricature_detail_activity);
        setStatusbarColor(R.color.none);
        ButterKnife.bind(this);
        init();
        loadAD();
    }

    private void transparentStatusbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void init() {
        try {
            String serializedStr = getIntent().getStringExtra(KeyUtil.AVObjectKey);
            mAVObject = AVObject.parseAVObject(serializedStr);
            if (mAVObject != null) {
                itemImg.setImageURI(mAVObject.getString(AVOUtil.Caricature.book_img_url));
                item_img_bg.setImageURI(mAVObject.getString(AVOUtil.Caricature.book_img_url));
                name.setText(mAVObject.getString(AVOUtil.Caricature.name));
                tags.setText(mAVObject.getString(AVOUtil.Caricature.tag));
                author.setText(mAVObject.getString(AVOUtil.Caricature.author));
                des.setText(mAVObject.getString(AVOUtil.Caricature.des));
                views.setText("人气：" + mAVObject.getNumber(AVOUtil.Caricature.views));
                source.setText("来源：" + mAVObject.getString(AVOUtil.Caricature.source_name));
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAD(){
        mXFYSAD = new XFYSAD(this, xx_ad_layout, ADUtil.MRYJYSNRLAd);
        mXFYSAD.showAD();
    }

    private void onItemClick() {
        Intent intent = new Intent(this, WebViewForCaricatureActivity.class);
        intent.putExtra(KeyUtil.AVObjectKey, mAVObject.toString());
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        startActivity(intent);
        AVAnalytics.onEvent(this, "to_caricature_webview");
    }


    @OnClick(R.id.add_bookshelf)
    public void onAddBookshelfClicked() {
        DataBaseUtil.getInstance().updateOrInsertAVObject(
                AVOUtil.Caricature.Caricature,
                mAVObject,
                mAVObject.getString(AVOUtil.Caricature.name),
                System.currentTimeMillis());
        NYBus.get().post(new CaricatureEventAddBookshelf());
        ToastUtil.diaplayMesShort(this,getString(R.string.add_bookshelf_success));
    }

    @OnClick(R.id.to_read)
    public void onToReadClicked() {
        onItemClick();
    }

    @OnClick(R.id.back_btn)
    public void onBackBtnClicked() {
        onBackPressed();
    }
}
