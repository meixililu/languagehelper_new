package com.messi.languagehelper.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.R;
import com.messi.languagehelper.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcPybsMenuItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;

    public RcPybsMenuItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final String code) {
        name.setTextColor(context.getResources().getColor(R.color.load_blue));
        name.setText( code );
        cover.setOnClickListener(view -> onItemClick(code));
    }

    private void onItemClick(String code){
        LiveEventBus.get(KeyUtil.ChPybsType).post(code);
    }

}
