package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudyNewWordActivity;
import com.messi.languagehelper.WordStudyPlanActivity;
import com.messi.languagehelper.WordStudyPlanDetailActivity;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.SaveData;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudyHeaderViewHolder extends RecyclerView.ViewHolder {

    public View headerView;
    public FrameLayout wordStudyChangePlan;
    public FrameLayout word_study_new_word;
    public TextView wordStudyBookName;
    public ArcProgress arcProgress;
    private WordListItem wordListItem;
    private Context context;

    public RcWordStudyHeaderViewHolder(View itemView) {
        super(itemView);
        this.headerView = itemView;
        context = itemView.getContext();
        word_study_new_word = (FrameLayout)itemView.findViewById(R.id.word_study_new_word);
        wordStudyChangePlan = (FrameLayout)itemView.findViewById(R.id.word_study_change_plan);
        wordStudyBookName = (TextView)itemView.findViewById(R.id.word_study_book_name);
        arcProgress = (ArcProgress)itemView.findViewById(R.id.arc_progress);
    }

    public void render(){
        wordListItem = SaveData.getDataFonJson(context, KeyUtil.WordStudyUnit, WordListItem.class);
        if (wordListItem != null) {
            wordStudyBookName.setText(wordListItem.getTitle());
            arcProgress.setMax(wordListItem.getCourse_num());
            arcProgress.setProgress(wordListItem.getCourse_id());
        }
        itemView.setOnClickListener(view -> toWordStudyDetailActivity());
        wordStudyChangePlan.setOnClickListener(view -> toActivity(WordStudyPlanActivity.class));
        word_study_new_word.setOnClickListener(view -> toActivity(WordStudyNewWordActivity.class));
    }

    private void toActivity(Class mClass) {
        Intent intent = new Intent(context, mClass);
        context.startActivity(intent);
    }

    private void toWordStudyDetailActivity() {
        Intent intent = new Intent();
        Class toClass = null;
        if (wordListItem != null) {
            toClass = WordStudyPlanDetailActivity.class;
            intent.putExtra(KeyUtil.ActionbarTitle, wordListItem.getTitle());
        } else {
            toClass = WordStudyPlanActivity.class;
        }
        intent.setClass(context,toClass);
        context.startActivity(intent);
    }



}
