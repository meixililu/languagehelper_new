package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.SymbolDetailActivity;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.wxapi.WXEntryActivity;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudySummaryListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;

    public RcWordStudySummaryListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.HJWordStudyCList.word) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
//        WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mAVObject);
//        Intent intent = new Intent(context,SymbolDetailActivity.class);
//        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.HJWordStudyCList.title));
//        context.startActivity(intent);
    }

}
