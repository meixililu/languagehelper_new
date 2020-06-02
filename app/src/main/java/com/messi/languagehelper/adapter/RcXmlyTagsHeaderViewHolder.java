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
import com.ximalaya.ting.android.opensdk.model.tag.Tag;

import java.util.List;


/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyTagsHeaderViewHolder extends RecyclerView.ViewHolder {

    public View headerView;
    private Context context;
    private FlexboxLayout auto_wrap_layout;
    private List<Tag> list;
    private AdapterStringListener listener;

    public RcXmlyTagsHeaderViewHolder(View itemView,AdapterStringListener listener) {
        super(itemView);
        context = itemView.getContext();
        this.listener = listener;
        auto_wrap_layout = (FlexboxLayout) itemView.findViewById(R.id.auto_wrap_layout);
        if(list != null){
            setData(list);
        }
    }

    public void setData(List<Tag> tags){
        this.list = tags;
        auto_wrap_layout.removeAllViews();
        for (Tag tag : tags){
            auto_wrap_layout.addView(createNewFlexItemTextView(tag));
        }
    }

    private TextView createNewFlexItemTextView(final Tag book) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getTagName());
        textView.setTextSize(15);
        textView.setTextColor(context.getResources().getColor(R.color.text_black));
        if(book.getKind().equals("1")){
            textView.setBackgroundResource(R.drawable.bg_btn_orange);
        }else {
            textView.setBackgroundResource(R.drawable.bg_btn_gray);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
                book.setKind("1");
                if(listener != null){
                    listener.OnItemClick(book.getTagName());
                }
            }
        });
        int padding = ScreenUtil.dip2px(context,6);
        int paddingLeftAndRight = ScreenUtil.dip2px(context,6);
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
        for (Tag tag : list){
            tag.setKind("");
        }
    }
}
