package com.messi.languagehelper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.messi.languagehelper.adapter.XimalayaEnglishAdapter;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.LogUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.tag.Tag;
import com.ximalaya.ting.android.opensdk.model.tag.TagList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class XimalayaActivity extends BaseActivity implements FragmentProgressbarListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private XimalayaEnglishAdapter pageAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.joke_activity);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    private void initViews() {
        getSupportActionBar().setTitle("喜马拉雅fm");
    }

    private void initData() {
//        获取喜马拉雅内容分类
//        Map<String, String> map = new HashMap<String, String>();
//        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
//            @Override
//            public void onSuccess(CategoryList object) {
//                LogUtil.DefalutLog("onSuccess");
//                if(object != null){
//                    List<Category> list = object.getCategories();
//                    for (Category category : list) {
//                        LogUtil.DefalutLog(category.toString());
//                    }
//                }
//            }
//            @Override
//            public void onError(int code, String message) {
//                LogUtil.DefalutLog("onError:"+code+"---mes:"+message);
//            }
//        });

//        获取专辑标签或者声音标签
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, "38");
        map.put(DTransferConstants.TYPE, "0");
        CommonRequest.getTags(map, new IDataCallBack<TagList>() {
            @Override
            public void onSuccess(@Nullable TagList tagList) {
                LogUtil.DefalutLog("onSuccess");
                if (tagList != null) {
                    List<Tag> list = tagList.getTagList();
                    pageAdapter = new XimalayaEnglishAdapter(XimalayaActivity.this.getSupportFragmentManager(),
                            XimalayaActivity.this, list);
                    viewpager.setAdapter(pageAdapter);
                    viewpager.setOffscreenPageLimit(5);
                    tablayout.setupWithViewPager(viewpager);
                }
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.DefalutLog("onError:" + i + "---mes:" + s);
            }
        });

//        获取直播省市列表
//        Map<String, String> map = new HashMap<String, String>();
//        CommonRequest.getProvinces(map, new IDataCallBack<ProvinceList>(){
//            @Override
//            public void onSuccess(@Nullable ProvinceList provinceList) {
//                LogUtil.DefalutLog("onSuccess");
//                if(provinceList != null){
//                    List<Province> list = provinceList.getProvinceList();
//                    for (Province category : list) {
//                        LogUtil.DefalutLog(category.getProvinceId()+"");
//                        LogUtil.DefalutLog(category.getProvinceName());
//                        LogUtil.DefalutLog(category.getProvinceCode()+"");
//                    }
//                }
//            }
//            @Override
//            public void onError(int i, String s) {
//                LogUtil.DefalutLog("onError:"+i+"---mes:"+s);
//            }
//        });

    }

}
