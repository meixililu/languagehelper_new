package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ReadingsActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcBoutiquesViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView name;
    private final SimpleDraweeView list_item_img;
    private Context context;

    public RcBoutiquesViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        name = (TextView) itemView.findViewById(R.id.name);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.Boutiques.title) );
        if(TextUtils.isEmpty(mAVObject.getString(AVOUtil.Boutiques.img_url))){
            list_item_img.setVisibility(View.GONE);;
        }else{
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageURI(mAVObject.getString(AVOUtil.Boutiques.img_url));
        }
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context, ReadingsActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.Boutiques.title));
        intent.putExtra(KeyUtil.BoutiqueCode, mAVObject.getString(AVOUtil.Boutiques.code));
        context.startActivity(intent);
        mAVObject.increment(AVOUtil.Boutiques.views);
        mAVObject.saveInBackground();
    }

}
