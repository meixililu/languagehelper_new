package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.event.PybsEvent;

import org.greenrobot.eventbus.EventBus;

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
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(code);
            }
        });
    }

    private void onItemClick(String code){
        EventBus.getDefault().post(new PybsEvent(code));
    }

}
