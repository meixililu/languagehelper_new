package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.R;
import com.messi.languagehelper.SymbolDetailActivity;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.util.KeyUtil;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcSymbloListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private final TextView des;
    private Context context;
    private List<SymbolListDao> avObjects;

    public RcSymbloListItemViewHolder(View convertView, List<SymbolListDao> avObjects) {
        super(convertView);
        this.context = convertView.getContext();
        this.avObjects = avObjects;
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
        des = (TextView) convertView.findViewById(R.id.des);
    }

    public void render(final SymbolListDao mAVObject) {
        name.setText( mAVObject.getSDName() );
        des.setText( mAVObject.getSDDes() );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(SymbolListDao mAVObject){
        Setings.dataMap.put(KeyUtil.DataMapKey, mAVObject);
        Intent intent = new Intent(context,SymbolDetailActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getSDDes() + mAVObject.getSDName());
        context.startActivity(intent);
    }

}
