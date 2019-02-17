package com.messi.languagehelper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messi.languagehelper.impl.PracticeProgressListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;

import java.io.File;
import java.io.IOException;

import static com.messi.languagehelper.util.PlayUtil.mSharedPreferences;

public class FinishFragment extends BaseFragment implements OnClickListener {

    protected SoundPool mSoundPoll;
    private TextView share_content;
    private FrameLayout share_btn_cover, check_btn;
    private LinearLayout parent_layout;
    private String shareContent;
    private int mSoundId;
    private PracticeProgressListener mPracticeProgress;
    private SharedPreferences sp;

    public static FinishFragment newInstance(PracticeProgressListener mPracticeProgress) {
        FinishFragment fragment = new FinishFragment();
        fragment.setData(mPracticeProgress);
        return fragment;
    }

    public void setData(PracticeProgressListener mPracticeProgress) {
        this.mPracticeProgress = mPracticeProgress;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.finish_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        sp = Setings.getSharedPreferences(getContext());
        parent_layout = (LinearLayout) getView().findViewById(R.id.parent_layout);
        share_content = (TextView) getView().findViewById(R.id.share_content);
        share_btn_cover = (FrameLayout) getView().findViewById(R.id.share_btn_cover);
        check_btn = (FrameLayout) getView().findViewById(R.id.check_btn);
        mSoundPoll = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        mSoundId = mSoundPoll.load(getActivity(), R.raw.camera, 1);

        share_btn_cover.setOnClickListener(this);
        check_btn.setOnClickListener(this);

        int currentSection = mSharedPreferences.getInt(KeyUtil.AiBaseCurrentSection,0);
        Setings.saveSharedPreferences(sp,KeyUtil.AiBaseCurrentSection,(currentSection+1));
    }

    private void shareWithImg() throws IOException {
        share_content.setFocusable(false);
        parent_layout.setDrawingCacheEnabled(true);
        parent_layout.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        parent_layout.layout(0, 0, parent_layout.getMeasuredWidth(), parent_layout.getMeasuredHeight());
        parent_layout.buildDrawingCache();
        Bitmap bitmap = parent_layout.getDrawingCache();
        if (bitmap != null) {
            String imgPath = SDCardUtil.saveBitmap(getActivity(), bitmap);
            File file = new File(imgPath);
            if (file != null && file.exists() && file.isFile()) {
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.share));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(Intent.createChooser(intent, this.getResources().getString(R.string.share)));
            }
        } else {
            LogUtil.DefalutLog("bitmap == null");
        }
        share_content.setFocusable(true);
        parent_layout.requestLayout();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn_cover:
                share();
                AVAnalytics.onEvent(getActivity(), "finish_pg_share_btn");
                break;
            case R.id.check_btn:
                toNextPage();
                break;
            default:
                break;
        }
    }

    private void playMusic() {
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Activity.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        mSoundPoll.play(mSoundId, volume, volume, 1, 0, 1f);
    }

    private void share() {
        try {
            playMusic();
            shareContent = share_content.getText().toString();
            if (!TextUtils.isEmpty(shareContent)) {
                shareWithImg();
            } else {
                ToastUtil.diaplayMesShort(getActivity(), R.string.share_text_hint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toNextPage() {
        if (mPracticeProgress != null) {
            mPracticeProgress.finishActivity();
        }
    }


}
