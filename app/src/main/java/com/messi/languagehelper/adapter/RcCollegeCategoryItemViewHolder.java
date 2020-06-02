package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.BoutiquesFragment;
import com.messi.languagehelper.EmptyActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcCollegeCategoryItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final SimpleDraweeView item_img;
    private Context context;

    public RcCollegeCategoryItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        item_img = (SimpleDraweeView) convertView.findViewById(R.id.item_img);
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.BoutiquesClass.name) );
        item_img.setImageURI(mAVObject.getString(AVOUtil.BoutiquesClass.img));
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context, EmptyActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(KeyUtil.FragmentName, BoutiquesFragment.class);
        args.putString(KeyUtil.Type,mAVObject.getString(AVOUtil.BoutiquesClass.type));
        args.putString(KeyUtil.ActionbarTitle,mAVObject.getString(AVOUtil.BoutiquesClass.name));
        intent.putExtra(KeyUtil.BundleKey,args);
        context.startActivity(intent);
        mAVObject.increment(AVOUtil.BoutiquesClass.views);
        mAVObject.saveInBackground();
    }

}
