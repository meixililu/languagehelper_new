package com.messi.languagehelper;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PybsDetailActivity extends BaseActivity {

    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.xx_ad_layout)
    FrameLayout xx_ad_layout;
    @BindView(R.id.scrollview)
    NestedScrollView scrollview;

    private AVObject mAVObject;
    private XFYSAD mXFYSAD;
    private String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pybs_detail_activity);
        ButterKnife.bind(this);
        initData();
        RequestAsyncTask();
        LoadAD();
    }

    private void initData() {
        code = getIntent().getStringExtra(KeyUtil.CodeKey);
        toolbar_layout.setTitle(code);
        title.setText(code);
    }

    private void RequestAsyncTask() {
        showProgressbar();
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XhDicSList.XhDicSList);
        query.whereEqualTo(AVOUtil.XhDicSList.name, code);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                hideProgressbar();
                if(list != null){
                    if(list.size() > 0){
                        mAVObject = list.get(0);
                        content.setText(mAVObject.getString(AVOUtil.XhDicSList.content));
                    }
                }else{
                    ToastUtil.diaplayMesShort(PybsDetailActivity.this, "加载失败，下拉可刷新");
                }
            }
        });
    }

    private void LoadAD() {
        mXFYSAD = new XFYSAD(this, xx_ad_layout, ADUtil.NewsDetail);
        mXFYSAD.setDirectExPosure(true);
        mXFYSAD.showAD();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pybs_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_share:
                copyOrshare();
                break;
        }
        return true;
    }

    private void copyOrshare() {
        StringBuilder sb = new StringBuilder();
        sb.append(code);
        sb.append("\n");
        sb.append(content.getText().toString());
        Settings.share(this, sb.toString());
    }
}
