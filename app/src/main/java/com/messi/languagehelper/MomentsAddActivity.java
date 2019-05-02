package com.messi.languagehelper;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;

public class MomentsAddActivity extends BaseActivity implements OnClickListener {

    private AppCompatEditText share_content;
    private FrameLayout share_btn_cover;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moments_add_layout);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_moments_add));
        share_content = (AppCompatEditText) findViewById(R.id.share_content);
        share_btn_cover = (FrameLayout) findViewById(R.id.share_btn_cover);
        share_btn_cover.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn_cover:
                checkUidAndPublish();
                break;
        }
    }

    private void checkUidAndPublish(){
        AVQuery<AVObject> avQuery = new AVQuery<>(AVOUtil.MomentsFilter.MomentsFilter);
        avQuery.whereEqualTo(AVOUtil.MomentsFilter.uid,SystemUtil.getDev_id(this));
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if(avObjects != null && avObjects.size() > 0){
                    ToastUtil.diaplayMesShort(MomentsAddActivity.this,"很抱歉，您已被禁言！");
                }else {
                    publish();
                }
            }
        });
    }

    private void publish() {
        String content = share_content.getText().toString().trim();
        if(isCanPublish(content)){
            AVObject object = new AVObject(AVOUtil.Moments.Moments);
            object.put(AVOUtil.Moments.content, content);
            object.put(AVOUtil.Moments.uid, SystemUtil.getDev_id(this));
            object.saveEventually(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if(e == null){
                        ToastUtil.diaplayMesShort(MomentsAddActivity.this,"发布成功");
                        setResult(RESULT_OK);
                        MomentsAddActivity.this.finish();
                    }else{
                        ToastUtil.diaplayMesShort(MomentsAddActivity.this,"发布失败，请重试！");
                    }
                }
            });
        }
    }

    private boolean isCanPublish(String content){
        boolean isCanPublish = true;
        if(TextUtils.isEmpty(content)){
            isCanPublish = false;
            ToastUtil.diaplayMesShort(MomentsAddActivity.this,"请输入！");
        }else if(!StringUtils.isAllEnglish(content)){
            isCanPublish = false;
            ToastUtil.diaplayMesShort(MomentsAddActivity.this,"只能输入英文！");
        }
        return isCanPublish;
    }

}
