package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcXmlyCategoryAdapter;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.views.DividerGridItemDecoration;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.category.Category;
import com.ximalaya.ting.android.opensdk.model.category.CategoryList;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlyCategoryFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 3;
    private List<Category> categories;
    private RecyclerView category_lv;
    private RcXmlyCategoryAdapter mAdapter;
    private SharedPreferences sharedPreferences;

    public static XmlyCategoryFragment getInstance() {
        return new XmlyCategoryFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.xmly_category_fragment, container, false);
        setHasOptionsMenu(true);
        initSwipeRefresh(view);
        initViews(view);
        getTagsData();
        return view;
    }

    private void initViews(View view) {
        sharedPreferences = Setings.getSharedPreferences(getContext());
        categories = new ArrayList<Category>();
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcXmlyCategoryAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setItems(categories);
        category_lv.setAdapter(mAdapter);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        QueryTask();
    }

    private void getTagsData() {
        Type type = new TypeToken<List<Category>>() {}.getType();
        List<Category> tagList = SaveData.getDataListFonJson(getContext(), "xmly_category", type);
        if (tagList != null) {
            onSwipeRefreshLayoutFinish();
            categories.clear();
            categories.addAll(tagList);
            mAdapter.notifyDataSetChanged();
        } else {
            QueryTask();
        }
    }

    private void QueryTask() {
        Map<String, String> map = new HashMap<String, String>();
        CommonRequest.getCategories(map, new IDataCallBack<CategoryList>() {
            @Override
            public void onSuccess(CategoryList object) {
                if(object != null){
                    onSwipeRefreshLayoutFinish();
                    categories.clear();
                    categories.addAll(object.getCategories());
                    mAdapter.notifyDataSetChanged();
                    saveData();
                }
            }
            @Override
            public void onError(int code, String message) {
                onSwipeRefreshLayoutFinish();
            }
        });
    }

    private void saveData(){
        SaveData.saveDataListAsJson(getContext(), "xmly_category",categories);
    }




}
