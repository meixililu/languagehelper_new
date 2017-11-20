package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.google.gson.reflect.TypeToken;
import com.messi.languagehelper.adapter.XimalayaEnglishAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.XimalayaUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XimalayaEnglishActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private XimalayaEnglishAdapter pageAdapter;
    private List<Tag> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ximalaya_english_activity);
        ButterKnife.bind(this);
        initViews();
        getData();
    }

    private void initViews() {
        getSupportActionBar().setTitle("英语FM");
        list = new ArrayList<Tag>();
    }

    private void RequestData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, XimalayaUtil.Category_Eng);
        map.put(DTransferConstants.TYPE, "0");
        showProgressbar();
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(@Nullable TagList tagList) {
                hideProgressbar();
                if (tagList != null) {
                    list.clear();
                    list.addAll(tagList.getTagList());
                    initData();
                    saveData();
                }
            }

            @Override
            public void onError(int i, String s) {
                hideProgressbar();
                LogUtil.DefalutLog("onError:" + i + "---mes:" + s);
            }
        });
    }

    private void initData(){
        pageAdapter = new XimalayaEnglishAdapter(XimalayaEnglishActivity.this.getSupportFragmentManager(),
                XimalayaEnglishActivity.this, list);
        viewpager.setAdapter(pageAdapter);
        viewpager.setOffscreenPageLimit(10);
        tablayout.setupWithViewPager(viewpager);
    }

    private void saveData(){
        SaveData.saveDataListAsJson(this,"XimalayaEnglishActivity",list);
    }

    private void getData(){
        Type type = new TypeToken<List<Tag>>(){}.getType();
        List<Tag> tagList = SaveData.getDataListFonJson(this,"XimalayaEnglishActivity",type);
        if(tagList != null){
            list.clear();
            list.addAll(tagList);
            initData();
        }else {
            RequestData();
        }
    }

}
