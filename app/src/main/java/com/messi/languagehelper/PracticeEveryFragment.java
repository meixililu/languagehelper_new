package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechSynthesizer;
import com.messi.languagehelper.adapter.ViewPagerAdapter;
import com.messi.languagehelper.impl.PracticeProgressListener;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.SpannableStringUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.util.XFUtil;

import java.util.ArrayList;

public class PracticeEveryFragment extends BaseFragment implements OnClickListener {

    private View view;
    private String content;
    private FrameLayout check_btn_cover;
    private TextView check_btn;
    private PracticeProgressListener mPracticeProgress;
    private String[] yb, cn, en;
    private boolean isGoNext;
    private String videoPath;
    private ViewPager viewpager;
    private ArrayList<View> mViews;
    private LinearLayout viewpager_dot_layout;
    private SpeechSynthesizer mSpeechSynthesizer;
    private SharedPreferences mSharedPreferences;
    private LayoutInflater minflater;
    private int pageSize;
    private int currentIndex;


    public static PracticeEveryFragment newInstance(String content, PracticeProgressListener mPracticeProgress, String videoPath,
                                                    SharedPreferences mSharedPreferences, SpeechSynthesizer mSpeechSynthesizer) {
        PracticeEveryFragment fragment = new PracticeEveryFragment();
        fragment.setData(content, mPracticeProgress, videoPath, mSharedPreferences, mSpeechSynthesizer);
        return fragment;
    }

    public void setData(String content, PracticeProgressListener mPracticeProgress, String videoPath,
                        SharedPreferences mSharedPreferences, SpeechSynthesizer mSpeechSynthesizer) {
        this.content = content;
        this.mPracticeProgress = mPracticeProgress;
        getContent();
        this.videoPath = SDCardUtil.getDownloadPath(videoPath);
        this.mSpeechSynthesizer = mSpeechSynthesizer;
        this.mSharedPreferences = mSharedPreferences;
    }

    private void getContent() {
        String temp[] = content.split("#");
        if (temp.length > 3) {
            yb = temp[0].split(",");
            cn = temp[1].split(",");
            en = temp[2].split(",");
        } else {
            cn = temp[0].split(",");
            en = temp[1].split(",");
        }
        pageSize = cn.length;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.practice_every_fragment, container, false);
        minflater = inflater;
        initViews();
        return view;
    }

    private void initViews() {
        viewpager = (ViewPager) view.findViewById(R.id.viewpager);
        viewpager_dot_layout = (LinearLayout) view.findViewById(R.id.viewpager_dot_layout);
        check_btn_cover = (FrameLayout) view.findViewById(R.id.check_btn_cover);
        check_btn = (TextView) view.findViewById(R.id.check_btn);
        mViews = new ArrayList<View>();
        ViewUtil.addDot(getActivity(), en.length, viewpager_dot_layout);
        initItem();
        check_btn_cover.setOnClickListener(this);
    }

    private void initItem() {
        for (int i = 0; i < pageSize; i++) {
            View itemView = minflater.inflate(R.layout.practice_every_item, null, false);
            FrameLayout cover = (FrameLayout) itemView.findViewById(R.id.cover);
            TextView titleTv = (TextView) itemView.findViewById(R.id.title_tv);
            SpannableStringBuilder title = new SpannableStringBuilder();
            if (yb != null) {
                title.append(en[i] + "\n");
                title.append(SpannableStringUtil.setTextSize(getActivity(), yb[i], R.dimen.bigger));
                title.append("\n");
                title.append(SpannableStringUtil.setTextSize(getActivity(), cn[i], R.dimen.bigest));
            } else {
                title.append(en[i] + "\n");
                title.append(SpannableStringUtil.setTextSize(getActivity(), cn[i], R.dimen.bigest));
            }
            titleTv.setText(title);
            cover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVideo(currentIndex);
                }
            });
            mViews.add(itemView);
        }
        ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(mViews);
        viewpager.setAdapter(myPagerAdapter);
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_btn_cover:
                submit();
                break;
        }
    }

    private void submit() {
        if (currentIndex < (pageSize - 1)) {
            viewpager.setCurrentItem(++currentIndex);
        } else {
            toNextPage();
        }
    }

    private void changeState(int pos) {
        if (viewpager_dot_layout.isShown()) {
            int size = viewpager_dot_layout.getChildCount();
            for (int i = 0; i < size; i++) {
                View mView = viewpager_dot_layout.getChildAt(i);
                if (i == pos) {
                    mView.setEnabled(true);
                } else {
                    mView.setEnabled(false);
                }
            }
        }
    }

    private void setBtnText(int pos) {
        if (pos == (pageSize - 1)) {
            check_btn.setText(this.getResources().getString(R.string.practice_next_level));
        } else {
            check_btn.setText(this.getResources().getString(R.string.practice_next));
        }
    }

    private void toNextPage() {
        if (mPracticeProgress != null) {
            mPracticeProgress.toNextPage();
        }
    }

    private void playVideo(int position) {
        String filepath = videoPath + position + ".pcm";
        XFUtil.playVideoInBackground(getActivity(), mSpeechSynthesizer, mSharedPreferences, filepath, en[position]);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.destroy();
        }
    }

    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int pos) {
            currentIndex = pos;
            changeState(pos);
            setBtnText(pos);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }
}
