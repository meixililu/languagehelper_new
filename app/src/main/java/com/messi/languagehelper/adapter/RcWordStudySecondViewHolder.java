package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudyThirdActivity;
import com.messi.languagehelper.bean.WordListType;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.Setings;

/**
 * Created by luli on 10/23/16.
 */

public class RcWordStudySecondViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView name;
    private final TextView des;
    private final SimpleDraweeView list_item_img;
    private Context context;
    private String play_sign;

    public RcWordStudySecondViewHolder(View convertView, String play_sign) {
        super(convertView);
        this.context = convertView.getContext();
        this.play_sign = play_sign;
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        name = (TextView) itemView.findViewById(R.id.name);
        des = (TextView) itemView.findViewById(R.id.des);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);
    }

    public void render(final WordListType mAVObject) {
        name.setText( mAVObject.getTitle() );
        des.setText( mAVObject.getCourse_num() + "课    " + mAVObject.getWord_num() + "词");
        if(TextUtils.isEmpty(mAVObject.getImg_url())){
            list_item_img.setVisibility(View.GONE);;
        }else{
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageURI(mAVObject.getImg_url());
        }
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(WordListType mAVObject){
        Setings.dataMap.put(KeyUtil.DataMapKey, mAVObject.getItemList());
        Intent intent = new Intent(context, WordStudyThirdActivity.class);
        if(!TextUtils.isEmpty(play_sign)){
            intent.putExtra(KeyUtil.WordStudyPlan, play_sign);
        }
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
        context.startActivity(intent);
    }

}
