package com.messi.languagehelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.google.android.flexbox.FlexboxLayout;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.Setings;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.word.HotWord;
import com.ximalaya.ting.android.opensdk.model.word.HotWordList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.AVException;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.callback.FindCallback;
import cn.leancloud.convertor.ObserverBuilder;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class XmlySearchActivity extends BaseActivity {

    @BindView(R.id.search_et)
    EditText searchEt;
    @BindView(R.id.search_btn)
    FrameLayout searchBtn;
    @BindView(R.id.auto_wrap_layout)
    FlexboxLayout auto_wrap_layout;
    @BindView(R.id.clear_history)
    FrameLayout clearHistory;
    private long lastTime;
    private List<AVObject> words;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmly_search);
        ButterKnife.bind(this);
        init();
        addHistory();
        getHotWords();
        QueryTask();
    }

    private void init() {
        hideTitle();
        words = new ArrayList<AVObject>();
        searchEt.requestFocus();
        searchEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    if (System.currentTimeMillis() - lastTime > 1000) {
                        search(searchEt.getText().toString());
                    }
                    lastTime = System.currentTimeMillis();
                }
                return false;
            }
        });
    }

    private void addHistory(){
        String history_str = Setings.getSharedPreferences(this).getString(KeyUtil.XmlySearchHistory,"");
        if (!TextUtils.isEmpty(history_str)) {
            String[] hiss = history_str.split(",");
            if(hiss.length > 0){
                List<AVObject> avObjects = new ArrayList<AVObject>();
                for (int i=0; i<6; i++){
                    if(i < hiss.length) {
                        if(!TextUtils.isEmpty(hiss[i])){
                            AVObject avobj = new AVObject();
                            avobj.put(AVOUtil.XmlySearchHot.name, hiss[i]);
                            avObjects.add(avobj);
                        }
                    }
                }
                setData(avObjects);
            }
        }
    }

    public void OnItemClick(String item) {
        search(item);
    }

    private void getHotWords(){
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.TOP, "20");
        CommonRequest.getHotWords(map, new IDataCallBack<HotWordList>(){
            @Override
            public void onSuccess(@Nullable HotWordList hotWordList) {
                if(hotWordList != null && hotWordList.getHotWordList() != null){
                    List<AVObject> avObjects = new ArrayList<AVObject>();
                    for (HotWord hotWord : hotWordList.getHotWordList()){
                        boolean isAdd = true;
                        for (AVObject history : words){
                            if(history.getString(AVOUtil.SearchHot.name).equals(
                                    hotWord.getSearchword())){
                                isAdd = false;
                                break;
                            }
                        }
                        if (isAdd) {
                            AVObject avobj = new AVObject();
                            avobj.put(AVOUtil.XmlySearchHot.name, hotWord.getSearchword());
                            avObjects.add(avobj);
                        }
                    }
                    setData(avObjects);
                }
            }
            @Override
            public void onError(int i, String s) {

            }
        });
    }

    private void QueryTask(){
        AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.XmlySearchHot.XmlySearchHot);
        if(getPackageName().equals(Setings.application_id_zyhy)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"zyhy");
        }else if(getPackageName().equals(Setings.application_id_zyhy_google)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"zyhy");
        }else if(getPackageName().equals(Setings.application_id_yys)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"yys");
        }else if(getPackageName().equals(Setings.application_id_yys_google)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"yys");
        }else if(getPackageName().equals(Setings.application_id_yyj)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"zyhy");
        }else if(getPackageName().equals(Setings.application_id_yyj_google)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"zyhy");
        }else if(getPackageName().equals(Setings.application_id_ywcd)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"yycd");
        }else if(getPackageName().equals(Setings.application_id_xbky)){
            query.whereContains(AVOUtil.XmlySearchHot.app,"zyhy");
        }else{
            query.whereContains(AVOUtil.XmlySearchHot.app,"zyhy");
        }
        query.orderByAscending(AVOUtil.XmlySearchHot.createdAt);
        query.orderByDescending(AVOUtil.XmlySearchHot.click_time);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<AVObject> avObject) {
                if(avObject.size() != 0){
                    List<AVObject> avObjects = new ArrayList<AVObject>();
                    for (AVObject object : avObject){
                        boolean isAdd = true;
                        for (AVObject history : words){
                            if(history.getString(AVOUtil.SearchHot.name).equals(
                                    object.getString(AVOUtil.SearchHot.name))){
                                isAdd = false;
                                break;
                            }
                        }
                        if (isAdd) {
                            AVObject avobj = new AVObject();
                            avobj.put(AVOUtil.XmlySearchHot.name, object.getString(AVOUtil.SearchHot.name));
                            avObjects.add(avobj);
                        }
                    }
                    setData(avObjects);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void setData(List<AVObject> tags){
        words.addAll(tags);
        for (AVObject tag : tags){
            auto_wrap_layout.addView(createNewFlexItemTextView(tag));
        }
    }

    private TextView createNewFlexItemTextView(final AVObject book) {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getString(AVOUtil.XmlySearchHot.name));
        textView.setTextSize(16);
        textView.setTextColor(this.getResources().getColor(R.color.text_black));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnItemClick(book.getString(AVOUtil.XmlySearchHot.name));
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
                KeyUtil.XmlySearchHistory,
                "");
        auto_wrap_layout.removeAllViews();
        getHotWords();
        QueryTask();
    }

    private void search(String quest) {
        if (!TextUtils.isEmpty(quest)) {
            Intent intent = new Intent(this, XmlySearchResultActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle, quest);
            intent.putExtra(KeyUtil.SearchKey, quest);
            intent.putExtra(KeyUtil.PositionKey, 2);
            startActivity(intent);
            saveHistory(quest);
        }
    }

    private void saveHistory(String quest){
        StringBuilder sb = new StringBuilder();
        sb.append(quest);
        String history_str = Setings.getSharedPreferences(this).getString(KeyUtil.XmlySearchHistory,"");
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
                KeyUtil.XmlySearchHistory,
                sb.toString());
        saveHistoryToServer(quest);
    }

    private void saveHistoryToServer(final String quest){
        new Thread(() -> checkAndSaveData(quest)).start();
    }

    private void checkAndSaveData(final String quest){
        AVQuery<AVObject> query = new AVQuery<>(AVOUtil.XmlySearchHot.XmlySearchHot);
        query.whereEqualTo(AVOUtil.XmlySearchHot.name, quest);
        query.findInBackground().subscribe(ObserverBuilder.buildCollectionObserver(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (!list.isEmpty()) {
                    AVObject mAVObject = list.get(0);
                    int times = mAVObject.getInt(AVOUtil.XmlySearchHot.click_time);
                    mAVObject.put(AVOUtil.XmlySearchHot.click_time,times+1);
                    mAVObject.saveInBackground();
                } else {
                    AVObject object = new AVObject(AVOUtil.XmlySearchHot.XmlySearchHot);
                    if(getPackageName().equals(Setings.application_id_zyhy)){
                        object.put(AVOUtil.XmlySearchHot.app,"zyhy");
                    }else if(getPackageName().equals(Setings.application_id_zyhy_google)){
                        object.put(AVOUtil.XmlySearchHot.app,"zyhy");
                    }else if(getPackageName().equals(Setings.application_id_yys)){
                        object.put(AVOUtil.XmlySearchHot.app,"yys");
                    }else if(getPackageName().equals(Setings.application_id_yys_google)){
                        object.put(AVOUtil.XmlySearchHot.app,"yys");
                    }else if(getPackageName().equals(Setings.application_id_yyj)){
                        object.put(AVOUtil.XmlySearchHot.app,"zyhy");
                    }else if(getPackageName().equals(Setings.application_id_yyj_google)){
                        object.put(AVOUtil.XmlySearchHot.app,"zyhy");
                    }else if(getPackageName().equals(Setings.application_id_ywcd)){
                        object.put(AVOUtil.XmlySearchHot.app,"yycd");
                    }else if(getPackageName().equals(Setings.application_id_xbky)){
                        object.put(AVOUtil.XmlySearchHot.app,"zyhy");
                    }else{
                        object.put(AVOUtil.XmlySearchHot.app,"zyhy");
                    }
                    object.put(AVOUtil.XmlySearchHot.name, quest);
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
