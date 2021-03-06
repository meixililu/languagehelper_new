package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.ImgUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.XFYSAD;

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
    @BindView(R.id.share_img)
    ImageView share_img;
    @BindView(R.id.item_layout)
    LinearLayout itemLayout;

    private CNWBean mAVObject;
    private XFYSAD mXFYSAD;
    private String sharePath;
    private String imgUrl;
    private String shareImgName = "share_img.jpg";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                try {
                    sharePath = SDCardUtil.getDownloadPath(SDCardUtil.ImgPath) + shareImgName;
                    ImgUtil.toBitmap(CaricatureDetailActivity.this,sharePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

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

    private void init() {
        try {
            mAVObject = getIntent().getParcelableExtra(KeyUtil.ObjectKey);
            mAVObject = BoxHelper.getNewestData(mAVObject);
            if (mAVObject != null) {
                initButton();
                imgUrl = mAVObject.getImg_url();
                itemImg.setImageURI(imgUrl);
                item_img_bg.setImageURI(imgUrl);
                name.setText(mAVObject.getTitle());
                tags.setText(mAVObject.getTag());
                author.setText(mAVObject.getAuthor());
                des.setText(mAVObject.getDes());
                views.setText("人气：" + NumberUtil.getNumberStr(mAVObject.getView()));
                source.setText("来源：" + mAVObject.getSource_name());
            } else {
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initButton(){
        if(mAVObject.getCollected() > 100){
            addBookshelf.setText(getString(R.string.add_bookshelf_already));
        }else {
            addBookshelf.setText(getString(R.string.add_bookshelf));
        }
    }

    private void loadAD() {
        mXFYSAD = new XFYSAD(this, xx_ad_layout, ADUtil.MRYJYSNRLAd);
        mXFYSAD.showAd();
    }

    private void onItemClick() {
        Intent intent = new Intent(this, WebViewForCaricatureActivity.class);
        intent.putExtra(KeyUtil.ObjectKey, mAVObject);
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        startActivityForResult(intent,10002);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10002){
            mAVObject = BoxHelper.getNewestData(mAVObject);
        }
    }

    private void shareImg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DownLoadUtil.downloadFile(imgUrl, SDCardUtil.ImgPath, shareImgName, mHandler);
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXFYSAD != null) {
            mXFYSAD.showAd();
        }
    }

    @OnClick(R.id.add_bookshelf)
    public void onAddBookshelfClicked() {
        if(mAVObject.getCollected() > 100){
            mAVObject.setCollected(0);
        }else {
            mAVObject.setCollected(System.currentTimeMillis());
        }
        mAVObject.setUpdateTime(System.currentTimeMillis());
        initButton();
        BoxHelper.updateCNWBean(mAVObject);
        LiveEventBus.get(KeyUtil.CaricatureEventAddBookshelf).post("add");
    }

    @OnClick(R.id.to_read)
    public void onToReadClicked() {
        onItemClick();
    }

    @OnClick(R.id.back_btn)
    public void onBackBtnClicked() {
        onBackPressed();
    }

    @OnClick(R.id.share_img)
    public void onViewClicked() {
        shareImg();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mXFYSAD != null){
            mXFYSAD.onDestroy();
        }
    }
}
