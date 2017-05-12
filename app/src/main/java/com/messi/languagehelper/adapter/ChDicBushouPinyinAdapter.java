package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.messi.languagehelper.ChDicBushouPinyinActivity;
import com.messi.languagehelper.ChDicBushouPinyinListActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.ChDicBushouPinyin;
import com.messi.languagehelper.util.KeyUtil;

import java.util.List;

public class ChDicBushouPinyinAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<ChDicBushouPinyin> mList;
    private String type;

    public ChDicBushouPinyinAdapter(Context mContext, List<ChDicBushouPinyin> mList, String type) {
        context = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mList = mList;
        this.type = type;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.chdic_bushoupinyin_item, null);
            holder = new ViewHolder();
            holder.cover = (View) convertView.findViewById(R.id.layout_cover);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ChDicBushouPinyin mAVObject = mList.get(position);
        if (type.equals(ChDicBushouPinyinActivity.bushou)) {
            holder.name.setText(mAVObject.getBushou());
        } else {
            holder.name.setText(mAVObject.getPinyin());
        }
        holder.cover.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
        return convertView;
    }

    private void onItemClick(ChDicBushouPinyin mAVObject) {
        Intent intent = new Intent(context, ChDicBushouPinyinListActivity.class);
        intent.putExtra(KeyUtil.CHDicType, type);
        if (type.equals(ChDicBushouPinyinActivity.bushou)) {
            intent.putExtra(KeyUtil.CHDicWord, mAVObject.getBushou());
        } else {
            intent.putExtra(KeyUtil.CHDicWord, mAVObject.getPinyin());
        }
        context.startActivity(intent);
    }

    static class ViewHolder {
        View cover;
        TextView name;
    }


}
