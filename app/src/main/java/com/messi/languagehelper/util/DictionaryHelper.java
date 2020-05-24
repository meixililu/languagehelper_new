package com.messi.languagehelper.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.impl.DicHelperListener;

import java.util.regex.Pattern;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by luli on 10/02/2017.
 */

public class DictionaryHelper {

    public static void addDicContent(Context mContext, LinearLayout parentsLayout, Dictionary result,
                                     DicHelperListener listener){
        parentsLayout.removeAllViews();
        addTitle(mContext,parentsLayout,result,listener);
        if(result != null){
            String[] re_contents = result.getResult().split("\n\n");
            for (int i = 0;i<re_contents.length; i++){
                addContent(mContext,parentsLayout,re_contents[i],i);
            }
        }
        getListFooterView(mContext,parentsLayout);
    }

    public static void addDicContentForDialog(Context mContext, LinearLayout parentsLayout, Dictionary result,
                                     DicHelperListener listener){
        parentsLayout.removeAllViews();
        addTitle(mContext,parentsLayout,result,listener);
        if(result != null){
            String[] re_contents = result.getResult().split("\n\n");
            if(re_contents.length > 0){
                if(re_contents[0].length() > 500){
                    LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(MATCH_PARENT,ScreenUtil.dip2px(mContext, 400));
                    parentsLayout.setLayoutParams(mParams);
                }
                addContent(mContext,parentsLayout,re_contents[0],0);
            }
        }
    }

    private static void addTitle(final Context mContext, LinearLayout parentsLayout,
                                 final Dictionary result,final DicHelperListener listener){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dic_title_layout, null);
        FrameLayout dic_title_layout = (FrameLayout) view.findViewById(R.id.dic_title_layout);
        TextView title = (TextView) view.findViewById(R.id.dic_title);
        final CheckBox collect_btn = (CheckBox) view.findViewById(R.id.collected_cb);
        if(result != null){
            if (!TextUtils.isEmpty(result.getWord_name())) {
                title.setText(result.getWord_name());
            }
        }
        setIsCollected(collect_btn, result);
        collect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCollectedStatus(mContext,collect_btn,result);
            }
        });
        dic_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.playPcm(result,false,"");
                }
            }
        });
        parentsLayout.addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private static void addContent(Context mContext, LinearLayout parentsLayout, String content,
                                      int index){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dic_content_layout, null);
        FrameLayout dic_content_title_layout = (FrameLayout) view.findViewById(R.id.dic_content_title_layout);
        TextView dic_content_title = (TextView) view.findViewById(R.id.dic_content_title);
        View space = (View) view.findViewById(R.id.space);
        final TextView dic_content_tv = (TextView) view.findViewById(R.id.dic_content_tv);
        if(index == 0 && !content.startsWith("\n")){
            dic_content_title.setVisibility(View.GONE);
            space.setVisibility(View.GONE);
            dic_content_tv.setText(content);
            dic_content_tv.setVisibility(View.VISIBLE);
        }else {
            dic_content_title.setVisibility(View.VISIBLE);
            space.setVisibility(View.VISIBLE);
            if(content.startsWith("\n")){
                content = content.substring(content.indexOf("\n")+1);
            }
            String[] contents = content.split("\n");
            if(contents.length > 1){
                dic_content_title.setText(contents[0]);
                dic_content_tv.setText(content.substring(content.indexOf("\n")+1));
            }else {
                dic_content_title.setVisibility(View.GONE);
                space.setVisibility(View.GONE);
                dic_content_tv.setText(content);
            }
        }
        final String playContent = content;
        dic_content_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XFUtil.play(playContent,"");
            }
        });
        parentsLayout.addView(view,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private static void updateCollectedStatus(Context mContext, CheckBox collected_cb, Dictionary mBean){
        if(mBean != null && collected_cb != null){
            if (mBean.getIscollected().equals("0")) {
                mBean.setIscollected("1");
                ToastUtil.diaplayMesShort(mContext,mContext.getResources().getString(R.string.favorite_success));
                addToNewword(mBean);
            } else {
                mBean.setIscollected("0");
                ToastUtil.diaplayMesShort(mContext,mContext.getResources().getString(R.string.favorite_cancle));
            }
            setIsCollected(collected_cb,mBean);
            BoxHelper.update(mBean);
        }
    }

    private static void addToNewword(Dictionary mBean){
        String[] temps = mBean.getResult().split("\n\n");
        String result = "";
        if(temps.length > 0){
            result = temps[0].trim();
        }else {
            result = mBean.getResult();
        }
        WordDetailListItem myNewWord = new WordDetailListItem();
        myNewWord.setName(mBean.getWord_name());
        myNewWord.setDesc(result);
        String pats1 = "英.*\\[";
        Pattern pattern1 = Pattern.compile(pats1);
        String pats2 = "美.*\\[";
        Pattern pattern2 = Pattern.compile(pats2);
//        LogUtil.DefalutLog("pattern1:"+pattern1.matcher(result).find());
//        LogUtil.DefalutLog("pattern2:"+pattern2.matcher(result).find());
        if(pattern1.matcher(result).find() || pattern2.matcher(result).find()){
            String[] texts = result.split("\n");
            if (texts != null && texts.length > 0) {
                String symbol = texts[0];
                if(symbol.contains("英") || symbol.contains("美")){
                    String des = result.replace(symbol,"").trim();
                    myNewWord.setSymbol(symbol);
                    myNewWord.setDesc(des);
                }
            }
        }
        myNewWord.setNew_words("1");
        myNewWord.setType("search");
        BoxHelper.saveSearchResultToNewWord(myNewWord);
    }

    private static void setIsCollected(CheckBox collected_cb, Dictionary mBean){
        if(mBean != null && collected_cb != null){
            if ("0".equals(mBean.getIscollected())) {
                collected_cb.setChecked(false);
            } else {
                collected_cb.setChecked(true);
            }
        }
    }

    public static void getListFooterView(Context mContext,LinearLayout parentsLayout){
        View mView = new View(mContext);
        mView.setBackgroundResource(R.color.none);
        AbsListView.LayoutParams mparam = new AbsListView.LayoutParams(MATCH_PARENT, ScreenUtil.dip2px(mContext, 50));
        mView.setLayoutParams(mparam);
        parentsLayout.addView(mView);
    }


}
