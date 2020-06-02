package com.messi.languagehelper.adapter;

import android.content.Context;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.messi.languagehelper.R;
import com.messi.languagehelper.impl.AdapterStringListener;
import com.messi.languagehelper.util.ScreenUtil;
import com.ximalaya.ting.android.opensdk.model.live.provinces.Province;

import java.util.List;


/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyRadioProvinceHeaderViewHolder extends RecyclerView.ViewHolder {

    public View headerView;
    private Context context;
    private FlexboxLayout auto_wrap_layout;
    private List<Province> list;
    private AdapterStringListener listener;

    public RcXmlyRadioProvinceHeaderViewHolder(View itemView, AdapterStringListener listener) {
        super(itemView);
        context = itemView.getContext();

        this.listener = listener;
        auto_wrap_layout = (FlexboxLayout) itemView.findViewById(R.id.auto_wrap_layout);
        if(list != null){
            setData(list);
        }
    }

    public void setData(List<Province> tags){
        this.list = tags;
        auto_wrap_layout.removeAllViews();
        for (Province tag : tags){
            auto_wrap_layout.addView(createNewFlexItemTextView(tag));
        }
    }

    private TextView createNewFlexItemTextView(final Province book) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getProvinceName());
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(R.color.text_black));
        if(book.getCreatedAt() == 1){
            textView.setBackgroundResource(R.drawable.bg_btn_orange);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                book.setCreatedAt(1);
                if(listener != null){
                    listener.OnItemClick(String.valueOf(book.getProvinceCode()));
                }
            }
        });
        int padding = ScreenUtil.dip2px(context,5);
        int paddingLeftAndRight = ScreenUtil.dip2px(context,9);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ScreenUtil.dip2px(context,5);
        int marginTop = ScreenUtil.dip2px(context,5);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private void reset(){
        for (Province tag : list){
            tag.setCreatedAt(0);
        }
    }
}
