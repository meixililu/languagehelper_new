package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.XimalayaTagsActiviry;
import com.messi.languagehelper.util.KeyUtil;
import com.ximalaya.ting.android.opensdk.model.category.Category;

/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyCategoryItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final SimpleDraweeView item_img;
    private Context context;

    public RcXmlyCategoryItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        item_img = (SimpleDraweeView) convertView.findViewById(R.id.item_img);
    }

    public void render(final Category mAVObject) {
        name.setText( mAVObject.getCategoryName() );
        item_img.setImageURI(mAVObject.getCoverUrlLarge());
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(Category mAVObject){
        Intent intent = new Intent(context,XimalayaTagsActiviry.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getCategoryName());
        intent.putExtra(KeyUtil.Category, String.valueOf(mAVObject.getId()));
        context.startActivity(intent);
    }

}
