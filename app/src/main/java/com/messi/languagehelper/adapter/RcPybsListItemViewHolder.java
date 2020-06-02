package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.messi.languagehelper.ChPybsSListActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcPybsListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;

    public RcPybsListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        name.setTextSize(18);
        name.setText( mAVObject.getString(AVOUtil.XhDicList.name) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,ChPybsSListActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.XhDicList.name));
        intent.putExtra(KeyUtil.CodeKey,mAVObject.getString(AVOUtil.XhDicList.name));
        intent.putExtra(KeyUtil.Type,mAVObject.getString(AVOUtil.XhDicList.type));
        context.startActivity(intent);
    }

}
