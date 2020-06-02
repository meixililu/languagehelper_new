package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.messi.languagehelper.AiDialoguePracticeActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.AVOUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenEnglishCategoryListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;

    public RcSpokenEnglishCategoryListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        String itemName = "";
        String itemNameOld = mAVObject.getString(AVOUtil.EvaluationCategory.ECName);
        name.setText( itemNameOld );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,AiDialoguePracticeActivity.class);
        intent.putExtra(AVOUtil.EvaluationCategory.ECCode, mAVObject.getString(AVOUtil.EvaluationCategory.ECCode));
        context.startActivity(intent);
    }

}
