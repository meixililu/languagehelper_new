package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.box.WordDetailListItem;
import com.messi.languagehelper.impl.DictionaryTranslateListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by luli on 10/23/16.
 */

public class RcDictionaryListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView record_answer;
    private FrameLayout record_question_cover;
    private FrameLayout record_answer_cover;
    private FrameLayout voice_play_layout;
    private TextView record_question;
    private FrameLayout copy_btn;
    private FrameLayout collected_btn;
    private FrameLayout delete_btn;
    private CheckBox collected_cb;
    private Context context;
    private List<Dictionary> mBeans;
    RcDictionaryListAdapter mAdapter;
    private DictionaryTranslateListener mDictionaryTranslateListener;

    public RcDictionaryListItemViewHolder(View convertView,
                                          Context context,
                                          List<Dictionary> mBeans,
                                          RcDictionaryListAdapter mAdapter,
                                          DictionaryTranslateListener mDictionaryTranslateListener) {
        super(convertView);
        this.context = context;
        this.mBeans = mBeans;
        this.mAdapter = mAdapter;
        this.mDictionaryTranslateListener = mDictionaryTranslateListener;
        record_question = (TextView) convertView.findViewById(R.id.record_question);
        record_answer = (TextView) convertView.findViewById(R.id.record_answer);
        collected_cb = (CheckBox) convertView.findViewById(R.id.collected_cb);
        record_question_cover = (FrameLayout) convertView.findViewById(R.id.record_question_cover);
        record_answer_cover = (FrameLayout) convertView.findViewById(R.id.record_answer_cover);
        voice_play_layout = (FrameLayout) convertView.findViewById(R.id.voice_play_layout);
        copy_btn = (FrameLayout) convertView.findViewById(R.id.copy_btn);
        collected_btn = (FrameLayout) convertView.findViewById(R.id.collected_btn);
        delete_btn = (FrameLayout) convertView.findViewById(R.id.delete_btn);
    }

    public void render(final Dictionary mBean) {
        record_question.setText(mBean.getWord_name());
        String[] temps = mBean.getResult().split("\n\n");
        String result = "";
        if(temps.length > 0){
            result = temps[0].trim();
        }else {
            result = mBean.getResult();
        }
        record_answer.setText(result);
        record_question_cover.setOnClickListener(view -> {
            MyPlayer.getInstance(context).start(mBean.getWord_name());
        });
        voice_play_layout.setOnClickListener(view -> {
            MyPlayer.getInstance(context).start(mBean.getWord_name());
        });
        if (mBean.getIscollected().equals("0")) {
            collected_cb.setChecked(false);
        } else {
            collected_cb.setChecked(true);
        }
        record_question_cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Setings.copy(context,mBean.getWord_name());
                return true;
            }
        });
        record_answer_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDictionaryTranslateListener != null){
                    mDictionaryTranslateListener.showItem(mBean);
                }
            }
        });
        final String finalResult = result;
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setings.copy(context,mBean.getWord_name() + "\n" + finalResult);
            }
        });
        collected_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCollectedStatus(mBean);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntity(getAdapterPosition());
            }
        });
    }

    public void deleteEntity(int position) {
        try {
            Dictionary mBean = mBeans.remove(position);
            mAdapter.notifyItemRemoved(position);
            BoxHelper.remove(mBean);
            ToastUtil.diaplayMesShort(context, context.getResources().getString(R.string.dele_success));
            SDCardUtil.deleteContent(mBean.getWord_name(),mBean.getResult(),mBean.getBackup1());
            AVAnalytics.onEvent(context, "tab2_delete_btn");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCollectedStatus(Dictionary mBean) {
        if (mBean.getIscollected().equals("0")) {
            mBean.setIscollected("1");
            ToastUtil.diaplayMesShort(context, context.getResources().getString(R.string.favorite_success));
            addToNewword(mBean);
        } else {
            mBean.setIscollected("0");
            ToastUtil.diaplayMesShort(context, context.getResources().getString(R.string.favorite_cancle));
        }
        mAdapter.notifyDataSetChanged();
        BoxHelper.update(mBean);
    }

    private void addToNewword(Dictionary mBean){
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
}
