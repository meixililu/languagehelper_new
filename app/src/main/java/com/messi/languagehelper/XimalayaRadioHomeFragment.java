package com.messi.languagehelper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.flexbox.FlexboxLayout;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ViewUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.live.radio.Radio;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategory;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioCategoryList;
import com.ximalaya.ting.android.opensdk.model.live.radio.RadioList;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class XimalayaRadioHomeFragment extends BaseFragment implements FragmentProgressbarListener {

    @BindView(R.id.fm_radio_local)
    FrameLayout fmRadioLocal;
    @BindView(R.id.fm_radio_country)
    FrameLayout fmRadioCountry;
    @BindView(R.id.fm_radio_province)
    FrameLayout fmRadioProvince;
    @BindView(R.id.fm_radio_internet)
    FrameLayout fmRadioInternet;
    Unbinder unbinder;
    @BindView(R.id.auto_wrap_layout)
    FlexboxLayout autoWrapLayout;
    @BindView(R.id.rank_radio_list)
    LinearLayout rankRadioList;
    private LayoutInflater inflater;

    public static Fragment newInstance(FragmentProgressbarListener listener) {
        XimalayaRadioHomeFragment fragment = new XimalayaRadioHomeFragment();
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.xmly_radio_home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        this.inflater = inflater;
        initViews();
        return view;
    }

    private void initViews() {
        getRadioType();
        getRankRadios();
    }

    private void getRadioType() {
        Map<String, String> map = new HashMap<String, String>();
        CommonRequest.getRadioCategory(map, new IDataCallBack<RadioCategoryList>() {
            @Override
            public void onSuccess(RadioCategoryList object) {
                if (object != null && object.getRadioCategories() != null) {
                    autoWrapLayout.removeAllViews();
                    for (RadioCategory radioCategory : object.getRadioCategories()) {
                        autoWrapLayout.addView(createNewFlexItemTextView(radioCategory));
                    }
                }
            }

            @Override
            public void onError(int code, String message) {
            }
        });
    }

    private TextView createNewFlexItemTextView(final RadioCategory book) {
        TextView textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(book.getRadioCategoryName());
        textView.setTextSize(16);
        textView.setTextColor(getContext().getResources().getColor(R.color.text_black));
        textView.setBackgroundResource(R.drawable.bg_btn_gray);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        int padding = ScreenUtil.dip2px(getContext(), 8);
        int paddingLeftAndRight = ScreenUtil.dip2px(getContext(), 8);
        ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding);
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = ScreenUtil.dip2px(getContext(), 5);
        int marginTop = ScreenUtil.dip2px(getContext(), 5);
        layoutParams.setMargins(margin, marginTop, margin, 0);
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    private void getRankRadios() {
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.RADIO_COUNT, "30");
        CommonRequest.getRankRadios(map, new IDataCallBack<RadioList>() {
            @Override
            public void onSuccess(@Nullable RadioList radioList) {
                hideProgressbar();
                if (radioList != null && radioList.getRadios() != null) {
                    rankRadioList.removeAllViews();
                    for (Radio radio : radioList.getRadios()) {
                        rankRadioList.addView(initAlbum(radio));
                        rankRadioList.addView(ViewUtil.getLine(getContext()));
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                hideProgressbar();
            }
        });
    }

    private View initAlbum(final Radio mAVObject) {
        View view = inflater.inflate(R.layout.ximalaya_list_item, null);
        FrameLayout layout_cover = (FrameLayout) view.findViewById(R.id.layout_cover);
        SimpleDraweeView list_item_img = (SimpleDraweeView) view.findViewById(R.id.list_item_img);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView sub_title = (TextView) view.findViewById(R.id.sub_title);
        TextView source_name = (TextView) view.findViewById(R.id.source_name);
        TextView type_name = (TextView) view.findViewById(R.id.type_name);
        title.setText(mAVObject.getRadioName());
        sub_title.setText("正在直播：" + mAVObject.getProgramName());
        source_name.setText(StringUtils.numToStrTimes(mAVObject.getRadioPlayCount()));
        type_name.setText("");
        list_item_img.setImageURI(Uri.parse(mAVObject.getCoverUrlLarge()));
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        source_name.setCompoundDrawables(drawable, null, null, null);
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRadio(mAVObject);
            }
        });
        return view;
    }

    private void playRadio(Radio mAVObject) {
        Intent intent = new Intent(getActivity(), XimalayaRadioDetailActivity.class);
        intent.putExtra(KeyUtil.XmlyRadio, mAVObject);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public void setListener(FragmentProgressbarListener listener) {
        mProgressbarListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.fm_radio_local, R.id.fm_radio_country, R.id.fm_radio_province, R.id.fm_radio_internet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fm_radio_local:
                break;
            case R.id.fm_radio_country:
                break;
            case R.id.fm_radio_province:
                break;
            case R.id.fm_radio_internet:
                break;
        }
    }
}
