package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.SpokenEndlishCategoryActivity;
import com.messi.languagehelper.impl.AdapterStringListener;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcSeachHistoryListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;
    private AdapterStringListener mlistener;

    public RcSeachHistoryListItemViewHolder(View convertView,AdapterStringListener mlistener) {
        super(convertView);
        this.context = convertView.getContext();
        this.mlistener = mlistener;
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.SearchHot.name) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        if(mlistener != null){
            mlistener.OnItemClick(mAVObject.getString(AVOUtil.SearchHot.name));
        }
    }

}
