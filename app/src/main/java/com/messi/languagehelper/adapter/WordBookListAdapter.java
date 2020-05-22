package com.messi.languagehelper.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.messi.languagehelper.BaseActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudyViewAllActivity;
import com.messi.languagehelper.bean.WordListItem;
import com.messi.languagehelper.event.UpdateWordStudyPlan;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.NumberUtil;
import com.messi.languagehelper.util.SaveData;
import com.messi.languagehelper.util.Setings;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class WordBookListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<WordListItem> avObjects;
    private String play_sign;
    private AlertDialog dialog;

    public WordBookListAdapter(Context mContext, List<WordListItem> avObjects,String play_sign) {
        context = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.avObjects = avObjects;
        this.play_sign = play_sign;
    }

    public int getCount() {
        return avObjects.size();
    }

    public WordListItem getItem(int position) {
        return avObjects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.word_study_third_list_item, null);
            holder = new ViewHolder();
            holder.cover = (View) convertView.findViewById(R.id.layout_cover);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WordListItem mAVObject = avObjects.get(position);
        holder.name.setText(mAVObject.getTitle());
        holder.cover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
        return convertView;
    }

    private void onItemClick(WordListItem mAVObject) {
        if(TextUtils.isEmpty(play_sign)){
            Setings.dataMap.put(KeyUtil.DataMapKey, mAVObject);
            Intent intent = new Intent(context, WordStudyViewAllActivity.class);
            intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
            context.startActivity(intent);
        }else {
            showConfirmDialog(mAVObject);
        }
    }

    private void showConfirmDialog(final WordListItem mAVObject){
        View view = mInflater.inflate(R.layout.dialog_word_study_book_confirm,null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.setView(view);
        dialog.setCancelable(true);
        ImageView dialog_close = (ImageView) view.findViewById(R.id.dialog_close);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        TextView dialog_content = (TextView) view.findViewById(R.id.dialog_content);
        FrameLayout sure_btn = (FrameLayout) view.findViewById(R.id.sure_btn);

        dialog_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog_title.setText(mAVObject.getTitle());
        dialog_content.setText("20个/关  " + mAVObject.getWord_num() + "个  "
                + "共" + (NumberUtil.StringToInt(mAVObject.getWord_num()) / 20) + "关");
        sure_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setWordBook(mAVObject);
            }
        });
        dialog.show();
    }

    private void setWordBook(WordListItem mAVObject){
        SaveData.saveDataAsJson(context, KeyUtil.WordStudyUnit, new Gson().toJson(mAVObject));
        Intent intent = new Intent();
        intent.setAction(BaseActivity.ActivityClose);
        context.sendBroadcast(intent);
        EventBus.getDefault().post(new UpdateWordStudyPlan());
    }

    static class ViewHolder {
        View cover;
        TextView name;
    }


}
