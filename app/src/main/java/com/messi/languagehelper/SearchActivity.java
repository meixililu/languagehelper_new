package com.messi.languagehelper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Setings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;

public class SearchActivity extends BaseActivity {

    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.search_btn)
    FrameLayout searchBtn;
    @BindView(R.id.auto_wrap_layout)
    FlexboxLayout auto_wrap_layout;
    @BindView(R.id.clear_history)
    FrameLayout clearHistory;
    private long lastTime;
    private ArrayList<AVObject> historyList;
    private ArrayList<AVObject> avObjects;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
        new QueryTask(this).execute();
    }

    private void init() {
        Bundle bundle = getIntent().getBundleExtra(KeyUtil.BundleKey);
        if (bundle != null) {
            position = bundle.getInt(KeyUtil.PositionKey,0);
        }
        hideTitle();
        searchEt.requestFocus();
        historyList = new ArrayList<AVObject>();
        avObjects = new ArrayList<AVObject>();
        addHistory();
        searchEt.setOnKeyListener((view, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_ENTER) {
                if (System.currentTimeMillis() - lastTime > 1000) {
                    search(searchEt.getText().toString());
                }
                lastTime = System.currentTimeMillis();
            }
            return false;
        });
    }

    private void addHistory() {
        if (avObjects != null && historyList != null) {
            historyList.clear();
            String history_str = Setings.getSharedPreferences(this).getString(KeyUtil.SearchHistory, "");
            if (!TextUtils.isEmpty(history_str)) {
                String[] hiss = history_str.split(",");
                if (hiss.length > 0) {
                    for (int i = 0; i < 6; i++) {
                        if (i < hiss.length) {
                            if (!TextUtils.isEmpty(hiss[i])) {
                                AVObject avobj = new AVObject();
                                avobj.put(AVOUtil.SearchHot.name, hiss[i]);
                                historyList.add(avobj);
                            }
                        }
                    }
                    avObjects.addAll(0, historyList);
                }
            }
        }
    }

    public void OnItemClick(String item) {
        search(item);
    }

    private class QueryTask extends AsyncTask<Void, Void,  List<AVObject>> {

        private WeakReference<SearchActivity> mainActivity;

        public QueryTask(SearchActivity mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SearchHot.SearchHot);
            query.orderByAscending(AVOUtil.SearchHot.createdAt);
            query.orderByDescending(AVOUtil.SearchHot.click_time);
            try {
                return query.find();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            if(mainActivity.get() != null && avObject != null){
                if(!avObject.isEmpty()){
                    if(avObjects != null){
                        for(AVObject obj : avObject){
                            boolean isAdd = true;
                            for (AVObject history : historyList){
                                if(history.getString(AVOUtil.SearchHot.name).equals(
                                        obj.getString(AVOUtil.SearchHot.name))){
                                    isAdd = false;
                                    break;
                                }
                            }
                            if (isAdd) {
                                avObjects.add(obj);
                            }
                        }
                        setData(avObjects);
                    }
                }
            }
        }
    }

    public void setData(List<AVObject> tags){
        auto_wrap_layout.removeAllViews();
        for (AVObject tag : tags){
            auto_wrap_layout.addView(createNewFlexItemTextView(tag));
        }
    }

    private TextView createNewFlexItemTextView(final AVObject book) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getString(AVOUtil.SearchHot.name));
        textView.setTextSize(16);
        textView.setTextColor(this.getResources().getColor(R.color.text_black));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClick(book.getString(AVOUtil.SearchHot.name));
            }
        });
        int padding = ScreenUtil.dip2px(this,5);
        int paddingLeftAndRight = ScreenUtil.dip2px(this,9);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ScreenUtil.dip2px(this,5);
        int marginTop = ScreenUtil.dip2px(this,5);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @OnClick({R.id.search_btn, R.id.clear_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_btn:
                search(searchEt.getText().toString());
                break;
            case R.id.clear_history:
                clearHistory();
                break;
        }
    }

    private void clearHistory(){
        Setings.saveSharedPreferences(Setings.getSharedPreferences(this),
                KeyUtil.SearchHistory,
                "");
        historyList.clear();
        avObjects.clear();
        new QueryTask(this).execute();
    }

    private void search(String quest) {
        if (!TextUtils.isEmpty(quest)) {
            Intent intent = new Intent(this, XmlySearchResultActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle, quest);
            intent.putExtra(KeyUtil.SearchKey, quest);
            intent.putExtra(KeyUtil.PositionKey, position);
            startActivity(intent);
            saveHistory(quest);
        }
    }

    private void saveHistory(String quest){
        StringBuilder sb = new StringBuilder();
        sb.append(quest);
        String history_str = Setings.getSharedPreferences(this).getString(KeyUtil.SearchHistory,"");
        if (!TextUtils.isEmpty(history_str)) {
            String[] hiss = history_str.split(",");
            if(hiss.length > 0){
                for (int i=0; i<6; i++){
                    if(i < hiss.length){
                        if(!quest.equals(hiss[i]) && !TextUtils.isEmpty(hiss[i])){
                            sb.append(",");
                            sb.append(hiss[i]);
                        }
                    }
                }
            }
        }
        Setings.saveSharedPreferences(Setings.getSharedPreferences(this),
                KeyUtil.SearchHistory,
                sb.toString());
        saveHistoryToServer(quest);
    }

    private void saveHistoryToServer(final String quest){
        new Thread(new Runnable() {
            @Override
            public void run() {
                checkAndSaveData(quest);
            }
        }).start();
    }

    private void checkAndSaveData(final String quest){
        AVQuery<AVObject> query = new AVQuery<>(AVOUtil.SearchHot.SearchHot);
        query.whereEqualTo(AVOUtil.SearchHot.name, quest);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null) {
                    if (list.size() > 0) {
                        AVObject mAVObject = list.get(0);
                        int times = mAVObject.getInt(AVOUtil.SearchHot.click_time);
                        mAVObject.put(AVOUtil.SearchHot.click_time,times+1);
                        mAVObject.saveInBackground();
                    }
                } else {
                    AVObject object = new AVObject(AVOUtil.SearchHot.SearchHot);
                    object.put(AVOUtil.SearchHot.name, quest);
                    object.saveInBackground();
                }
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideIME(searchEt);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideIME(searchEt);
    }

}
