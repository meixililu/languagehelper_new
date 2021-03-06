package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.ViewModel.LeisureModel;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CollectedData;
import com.messi.languagehelper.box.Reading;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.TextHandlerUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.json.JSON;

public class ReadingDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.ad_sign)
    TextView ad_sign;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.ad_layout)
    FrameLayout ad_layout;
    @BindView(R.id.ad_img)
    SimpleDraweeView ad_img;
    @BindView(R.id.next_composition)
    LinearLayout next_composition;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;
    @BindView(R.id.player_layout)
    LinearLayout player_layout;
    @BindView(R.id.btn_play)
    ImageView btn_play;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.time_current)
    TextView time_current;
    @BindView(R.id.time_duration)
    TextView time_duration;

    private Reading mAVObject;
    private List<Reading> mAVObjects;
    private SharedPreferences mSharedPreferences;
    private int index;
    private LeisureModel mLeisureModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.composition_detail_activity);
        registerBroadcast();
        ButterKnife.bind(this);
        initData();
        setData();
        guide();
    }

    private void initData() {
        mSharedPreferences = Setings.getSharedPreferences(this);
        index = getIntent().getIntExtra(KeyUtil.IndexKey, 0);
        Object data =  Setings.dataMap.get(KeyUtil.DataMapKey);
        Setings.dataMap.clear();
        if(data instanceof List){
            mAVObjects = (List<Reading>) data;
            if(mAVObjects != null && mAVObjects.size() > index){
                mAVObject = mAVObjects.get(index);
            }
        }
    }

    private void setData() {
        if (mAVObject == null) {
            finish();
            return;
        }
        toolbar_layout.setTitle(mAVObject.getTitle());
        title.setText(mAVObject.getTitle());
        scrollview.scrollTo(0, 0);
        TextHandlerUtil.handlerText(this, content, mAVObject.getContent());
        if (!TextUtils.isEmpty(mAVObject.getImg_url())) {
            ad_sign.setVisibility(View.GONE);
            ad_img.setImageURI(Uri.parse(mAVObject.getImg_url()));
        }else {
            mLeisureModel = new LeisureModel(this);
            mLeisureModel.setXFADID(ADUtil.NewsDetail);
            mLeisureModel.setViews(ad_sign,ad_img,xx_ad_layout,ad_layout);
            mLeisureModel.showAd();
        }
        if("text".equals(mAVObject.getType())){
            player_layout.setVisibility(View.GONE);
        }
        if(TextUtils.isEmpty(mAVObject.getStatus())){
            mAVObject.setStatus("1");
            BoxHelper.update(mAVObject);
        }
        if (BoxHelper.isCollected(mAVObject.getObject_id())) {
            mAVObject.setIsCollected("1");
        } else {
            mAVObject.setIsCollected("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.composition, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(menu.size() > 1){
            setMenuIcon(menu.getItem(1));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_share:
                copyOrshare(0);
                break;
            case R.id.action_collected:
                setCollectStatus();
                setMenuIcon(item);
                updateData();
                break;
        }
        return true;
    }

    private void setCollectStatus(){
        if(TextUtils.isEmpty(mAVObject.getIsCollected())){
            mAVObject.setIsCollected("1");
        } else {
            mAVObject.setIsCollected("");
        }
    }

    private void updateData(){
        new Thread(() -> {
            if(mAVObject != null){
                if(!TextUtils.isEmpty(mAVObject.getIsCollected())){
                    CollectedData cdata = new CollectedData();
                    cdata.setObjectId(mAVObject.getObject_id());
                    cdata.setName(mAVObject.getTitle());
                    cdata.setType(AVOUtil.Reading.Reading);
                    cdata.setJson(JSON.toJSONString(mAVObject));
                    BoxHelper.insert(cdata);
                }else {
                    CollectedData cdata = new CollectedData();
                    cdata.setObjectId(mAVObject.getObject_id());
                    BoxHelper.remove(cdata);
                }
                LiveEventBus.get(KeyUtil.UpdateCollectedData).post("");
            }
        }).start();
    }

    private void setMenuIcon(MenuItem item){
        if(TextUtils.isEmpty(mAVObject.getIsCollected())){
            item.setIcon(this.getResources().getDrawable(R.drawable.ic_uncollected_white));
        }else{
            item.setIcon(this.getResources().getDrawable(R.drawable.ic_collected_white));
        }
    }

    private void copyOrshare(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(mAVObject.getTitle());
        sb.append("\n");
        sb.append(mAVObject.getContent());
        if (i == 0) {
            Setings.share(this, sb.toString());
        } else {
            Setings.copy(this, sb.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBroadcast();
        if(mLeisureModel != null){
            mLeisureModel.onDestroy();
        }
    }

    private void guide() {
        if (!mSharedPreferences.getBoolean(KeyUtil.isReadingDetailGuideShow, false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setTitle("");
            builder.setMessage("点击英文单词即可查询词意。");
            builder.setPositiveButton("确认", null);
            AlertDialog dialog = builder.create();
            dialog.show();
            Setings.saveSharedPreferences(mSharedPreferences, KeyUtil.isReadingDetailGuideShow, true);
        }
    }
}
