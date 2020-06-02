package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.messi.languagehelper.R;
import com.messi.languagehelper.XimalayaRadioCategoryListActivity;
import com.messi.languagehelper.XimalayaRadioLocalActivity;
import com.messi.languagehelper.XimalayaRadioProvinceActivity;
import com.messi.languagehelper.XimalayaRadioTypeListActivity;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategory;

import java.util.List;


/**
 * Created by luli on 10/23/16.
 */

public class RcXmlyRadioHomeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public View headerView;
    private Context context;
    private FlexboxLayout auto_wrap_layout;
    private FrameLayout fm_radio_local;
    private FrameLayout fm_radio_country;
    private FrameLayout fm_radio_province;
    private FrameLayout fm_radio_internet;

    public RcXmlyRadioHomeHeaderViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        auto_wrap_layout = (FlexboxLayout) itemView.findViewById(R.id.auto_wrap_layout);
        fm_radio_local = (FrameLayout) itemView.findViewById(R.id.fm_radio_local);
        fm_radio_country = (FrameLayout) itemView.findViewById(R.id.fm_radio_country);
        fm_radio_province = (FrameLayout) itemView.findViewById(R.id.fm_radio_province);
        fm_radio_internet = (FrameLayout) itemView.findViewById(R.id.fm_radio_internet);
        fm_radio_local.setOnClickListener(this);
        fm_radio_country.setOnClickListener(this);
        fm_radio_province.setOnClickListener(this);
        fm_radio_internet.setOnClickListener(this);
    }

    public void setData(List<RadioCategory> tags){
        auto_wrap_layout.removeAllViews();
        for (RadioCategory tag : tags){
            auto_wrap_layout.addView(createNewFlexItemTextView(tag));
        }
    }

    private TextView createNewFlexItemTextView(final RadioCategory book) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getRadioCategoryName());
        textView.setTextSize(16);
        textView.setTextColor(context.getResources().getColor(R.color.text_black));
        textView.setBackgroundResource(R.drawable.bg_btn_gray);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRadioCategoryList(book);
            }
        });
        int padding = ScreenUtil.dip2px(context, 8);
        int paddingLeftAndRight = ScreenUtil.dip2px(context, 8);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ScreenUtil.dip2px(context, 5);
        int marginTop = ScreenUtil.dip2px(context, 5);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fm_radio_local:
                toRadioLocal(context.getResources().getString(R.string.radio_local));
                break;
            case R.id.fm_radio_country:
                toRadioTypeList("1",context.getResources().getString(R.string.radio_country));
                break;
            case R.id.fm_radio_province:
                toRadioProvince(context.getResources().getString(R.string.radio_province));
                break;
            case R.id.fm_radio_internet:
                toRadioTypeList("3",context.getResources().getString(R.string.radio_internet));
                break;
        }
    }

    private void toRadioProvince(String title){
        Intent intent = new Intent(context, XimalayaRadioProvinceActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle,title);
        context.startActivity(intent);
    }

    private void toRadioLocal(String title){
        Intent intent = new Intent(context, XimalayaRadioLocalActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle,title);
        context.startActivity(intent);
    }

    private void toRadioTypeList(String type,String title){
//        电台类型：1-国家台，2-省市台，3-网络台
        Intent intent = new Intent(context, XimalayaRadioTypeListActivity.class);
        intent.putExtra(KeyUtil.Type,type);
        intent.putExtra(KeyUtil.ActionbarTitle,title);
        context.startActivity(intent);
    }

    private void toRadioCategoryList(RadioCategory book){
        Intent intent = new Intent(context, XimalayaRadioCategoryListActivity.class);
        intent.putExtra(KeyUtil.Category,String.valueOf(book.getId()));
        intent.putExtra(KeyUtil.ActionbarTitle,book.getRadioCategoryName());
        context.startActivity(intent);
    }

}
