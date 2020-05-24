package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcAiTuringAdapter;
import com.messi.languagehelper.bean.AiTuringResult;
import com.messi.languagehelper.box.AiEntity;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.util.AiUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KaiPinAdUIModelCustom;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MD5;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.FormBody;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AiTuringActivity extends BaseActivity {

    @BindView(R.id.input_et)
    AppCompatEditText inputEt;
    @BindView(R.id.submit_btn_cover)
    CardView submitBtn;
    @BindView(R.id.content_lv)
    RecyclerView contentLv;
    @BindView(R.id.record_anim_img)
    ImageView recordAnimImg;
    @BindView(R.id.record_layout)
    LinearLayout recordLayout;
    @BindView(R.id.volume_img)
    ImageView volumeImg;
    @BindView(R.id.volume_btn)
    FrameLayout volumeBtn;
    @BindView(R.id.input_type_btn)
    ImageView inputTypeBtn;
    @BindView(R.id.input_type_layout)
    LinearLayout inputTypeLayout;
    @BindView(R.id.voice_btn)
    TextView voiceBtn;
    @BindView(R.id.voice_btn_cover)
    CardView voiceBtnCover;
    @BindView(R.id.mic_layout)
    LinearLayout micLayout;
    @BindView(R.id.keybord_layout)
    LinearLayout keybordLayout;
    @BindView(R.id.progress_tv)
    TextView progressTv;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;
    @BindView(R.id.ad_img)
    SimpleDraweeView adImg;
    @BindView(R.id.ad_source)
    TextView adSource;
    @BindView(R.id.ad_layout)
    RelativeLayout adLayout;
    @BindView(R.id.delete_btn)
    FrameLayout deleteBtn;
    @BindView(R.id.splash_container)
    FrameLayout splash_container;
    @BindView(R.id.skip_view)
    TextView skip_view;
    private List<AiEntity> beans;
    private LinearLayoutManager mLinearLayoutManager;
    private SpeechRecognizer recognizer;
    public RcAiTuringAdapter mAdapter;
    private SharedPreferences sp;
    private KaiPinAdUIModelCustom mKaiPinAdUIModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_turing_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initData();
        mKaiPinAdUIModel = new KaiPinAdUIModelCustom(this, adSource, adImg, adLayout,
                contentLv, numberProgressBar, progressTv, splash_container, skip_view);
    }

    private void initData() {
        setActionBarTitle(getResources().getString(R.string.title_tuling_ai));
        sp = Setings.getSharedPreferences(this);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        beans = new ArrayList<AiEntity>();
        beans.addAll(BoxHelper.getAiEntityList(AiUtil.Ai_Turing));
        mAdapter = new RcAiTuringAdapter(this, beans, mProgressbar);
        mAdapter.setItems(beans);
        mLinearLayoutManager = new LinearLayoutManager(this);
        if (beans.size() > 7) {
            mLinearLayoutManager.setStackFromEnd(true);
        }
        contentLv.setLayoutManager(mLinearLayoutManager);
        contentLv.setAdapter(mAdapter);
        if (sp.getBoolean(KeyUtil.IsAiTuringPlayVoice, true)) {
            volumeImg.setImageResource(R.drawable.ic_volume_on);
        } else {
            volumeImg.setImageResource(R.drawable.ic_volume_off);
        }
        if (PlayUtil.getSP().getBoolean(KeyUtil.IsAiChatShowKeybordLayout, true)) {
            showKeybordLayout();
        } else {
            showMicLayout();
        }
        keybordLayout.requestFocus();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        if(!beans.isEmpty()){
            List<AiEntity> list = BoxHelper.getAiEntityList(beans.get(0).getId(), AiUtil.Ai_Turing);
            if (!list.isEmpty()) {
                beans.addAll(0, list);
                mAdapter.notifyDataSetChanged();
                contentLv.scrollToPosition(list.size());
            }
        }
        onSwipeRefreshLayoutFinish();
    }

    @OnClick({R.id.volume_btn, R.id.submit_btn_cover, R.id.input_type_layout,
            R.id.voice_btn_cover,R.id.delete_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.volume_btn:
                boolean isPlay = !sp.getBoolean(KeyUtil.IsAiTuringPlayVoice, true);
                Setings.saveSharedPreferences(sp,
                        KeyUtil.IsAiTuringPlayVoice,
                        isPlay);
                if (isPlay) {
                    volumeImg.setImageResource(R.drawable.ic_volume_on);
                } else {
                    volumeImg.setImageResource(R.drawable.ic_volume_off);
                }
                break;
            case R.id.submit_btn_cover:
                submit();
                break;
            case R.id.input_type_layout:
                changeInputType();
                break;
            case R.id.voice_btn_cover:
                AiTuringActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
                break;
            case R.id.delete_btn:
                clear_all();
                break;
        }
    }

    private void clear_all(){
        beans.clear();
        mAdapter.notifyDataSetChanged();
        BoxHelper.deleteAiEntity(AiUtil.Ai_Turing);
    }

    private void submit() {
        String input = inputEt.getText().toString().trim();
        if (!TextUtils.isEmpty(input)) {
            String last = input.substring(input.length() - 1);
            if (",.!;:'，。！‘；：".contains(last)) {
                input = input.substring(0, input.length() - 1);
            }
            AiEntity mAiEntity = new AiEntity();
            mAiEntity.setRole(AiUtil.Role_User);
            mAiEntity.setContent_video_id(String.valueOf(System.currentTimeMillis()));
            mAiEntity.setContent(input);
            mAiEntity.setContent_type(AiUtil.Content_Type_Text);
            mAiEntity.setEntity_type(AiUtil.Entity_Type_Chat);
            mAiEntity.setAi_type(AiUtil.Ai_Turing);
            mAdapter.addEntity(beans.size(), mAiEntity);
            contentLv.scrollToPosition(beans.size() - 1);
            requestData(inputEt.getText().toString().trim());
            inputEt.setText("");
            BoxHelper.insertOrUpdate(mAiEntity);
        }
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void showIatDialog() {
        if (!recognizer.isListening()) {
            recordLayout.setVisibility(View.VISIBLE);
            inputEt.setText("");
            voiceBtn.setText(this.getResources().getText(R.string.click_and_finish));
            XFUtil.showSpeechRecognizer(this,
                    PlayUtil.getSP(),
                    recognizer,
                    recognizerListener,
                    XFUtil.VoiceEngineMD);
        } else {
            finishRecord();
            recognizer.stopListening();
            showProgressbar();
        }
    }

    private void finishRecord() {
        recordLayout.setVisibility(View.GONE);
        recordAnimImg.setBackgroundResource(R.drawable.speak_voice_1);
        voiceBtn.setText(this.getResources().getText(R.string.click_and_speak));
    }

    private void changeInputType() {
        if (keybordLayout.isShown()) {
            showMicLayout();
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsAiChatShowKeybordLayout, false);
            hideIME(inputEt);
        } else {
            showKeybordLayout();
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsAiChatShowKeybordLayout, true);
            showIME();
            inputEt.requestFocus();
        }
    }

    private void showKeybordLayout() {
        inputTypeBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_mic));
        keybordLayout.setVisibility(View.VISIBLE);
        micLayout.setVisibility(View.GONE);
    }

    private void showMicLayout() {
        inputTypeBtn.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_keybord_btn));
        keybordLayout.setVisibility(View.GONE);
        micLayout.setVisibility(View.VISIBLE);
    }

    private void requestData(String msg) {
        showProgressbar();
        FormBody formBody = new FormBody.Builder()
                .add("key", Setings.AiTuringApiKey)
                .add("info", msg)
                .add("userid", Setings.getDeviceID(this))
                .build();
        LanguagehelperHttpClient.post(Setings.AiTuringApi, formBody, new UICallback(this) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    LogUtil.DefalutLog(responseString);
                    if (JsonParser.isJson(responseString)) {
                        AiTuringResult mAiResult = JSON.parseObject(responseString, AiTuringResult.class);
                        addAiResult(mAiResult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(AiTuringActivity.this, AiTuringActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onSwipeRefreshLayoutFinish();
                hideProgressbar();
            }
        });
    }

    private void addAiResult(AiTuringResult mAiResult) {
        if (mAiResult != null) {
            AiEntity mAiEntity = new AiEntity();
            mAiEntity.setRole(AiUtil.Role_Machine);
            mAiEntity.setEntity_type(AiUtil.Entity_Type_Chat);
            mAiEntity.setAi_type(AiUtil.Ai_Turing);
            mAiEntity.setContent_video_id(String.valueOf(System.currentTimeMillis()));
            mAiEntity.setContent(mAiResult.getText());
            if (TextUtils.isEmpty(mAiResult.getUrl())) {
                mAiEntity.setContent_type(AiUtil.Content_Type_Text);
            } else {
                mAiEntity.setLink(mAiResult.getUrl());
                mAiEntity.setContent_type(AiUtil.Content_Type_Link);
            }
            mAdapter.addEntity(beans.size(), mAiEntity);
            contentLv.scrollToPosition(beans.size() - 1);
            if (sp.getBoolean(KeyUtil.IsAiTuringPlayVoice, true)) {
                playVideo(mAiEntity);
            }
            BoxHelper.insertOrUpdate(mAiEntity);
        }
    }

    public void playVideo(final AiEntity mAiEntity) {
        String filepath = "";
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        if (TextUtils.isEmpty(mAiEntity.getContent_video_id())) {
            mAiEntity.setContent_video_id(MD5.encode(mAiEntity.getContent()));
        }
        filepath = path + mAiEntity.getContent_video_id() + ".pcm";
        mAiEntity.setContent_video_path(filepath);
        PlayUtil.play(filepath, mAiEntity.getContent(), null,
                new SynthesizerListener() {
                    @Override
                    public void onSpeakResumed() {
                    }

                    @Override
                    public void onSpeakProgress(int arg0, int arg1, int arg2) {
                    }

                    @Override
                    public void onSpeakPaused() {
                    }

                    @Override
                    public void onSpeakBegin() {
                        hideProgressbar();
                        PlayUtil.onStartPlay();
                    }

                    @Override
                    public void onCompleted(SpeechError arg0) {
                        if (arg0 != null) {
                            ToastUtil.diaplayMesShort(AiTuringActivity.this, arg0.getErrorDescription());
                        }
                        BoxHelper.insertOrUpdate(mAiEntity);
                        PlayUtil.onFinishPlay();
                    }

                    @Override
                    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
                    }

                    @Override
                    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
                    }
                });
    }

    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(AiTuringActivity.this, err.getErrorDescription());
            hideProgressbar();
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            showProgressbar();
            finishRecord();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult");
            String text = JsonParser.parseIatResult(results.getResultString());
            inputEt.append(text.toLowerCase());
            inputEt.setSelection(inputEt.length());
            if (isLast) {
                finishRecord();
                submit();
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

        }

        @Override
        public void onVolumeChanged(int volume, byte[] arg1) {
            if (volume < 4) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_1);
            } else if (volume < 8) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_2);
            } else if (volume < 12) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_3);
            } else if (volume < 16) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_4);
            } else if (volume < 20) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_5);
            } else if (volume < 24) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_6);
            } else if (volume < 31) {
                recordAnimImg.setBackgroundResource(R.drawable.speak_voice_7);
            }
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AiTuringActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.RECORD_AUDIO)
    void onShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this,R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("温馨提示")
                .setMessage("需要授权才能使用。")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                }).show();
    }

    @OnPermissionDenied(Manifest.permission.RECORD_AUDIO)
    void onPerDenied() {
        ToastUtil.diaplayMesShort(this,"拒绝录音权限，无法使用语音功能！");
    }

}
