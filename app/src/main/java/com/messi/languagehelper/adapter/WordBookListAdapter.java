package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudyFourthActivity;
import com.messi.languagehelper.dao.WordListItem;
import com.messi.languagehelper.util.KeyUtil;

import java.util.List;

public class WordBookListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<WordListItem> avObjects;

    public WordBookListAdapter(Context mContext, List<WordListItem> avObjects) {
        context = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.avObjects = avObjects;
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
        WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mAVObject);
        Intent intent = new Intent(context, WordStudyFourthActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
        context.startActivity(intent);
    }

    static class ViewHolder {
        View cover;
        TextView name;
    }


}
