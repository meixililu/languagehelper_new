package com.messi.languagehelper;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.SystemUtil;
import com.messi.languagehelper.util.ToastUtil;

public class MomentsAddActivity extends BaseActivity implements OnClickListener {

    private EditText share_content;
    private FrameLayout share_btn_cover;
    private ScrollView parent_layout;
    private String shareContent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moments_add_layout);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_moments));
        parent_layout = (ScrollView) findViewById(R.id.parent_layout);
        share_content = (EditText) findViewById(R.id.share_content);
        share_btn_cover = (FrameLayout) findViewById(R.id.share_btn_cover);
        share_btn_cover.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn_cover:
                publish();
                break;
        }
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
        }
        return isCanPublish;
    }

}
