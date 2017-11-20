package com.messi.languagehelper;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.avos.avoscloud.AVAnalytics;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcAiChatAdapter;
import com.messi.languagehelper.bean.AiResult;
import com.messi.languagehelper.dao.AiEntity;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.OnFinishListener;
import com.messi.languagehelper.util.AiUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KaiPinAdUIModelCustom;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AiChatActivity extends BaseActivity {

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
    @BindView(R.id.speak_language_tv)
    TextView speakLanguageTv;
    @BindView(R.id.speak_language_layout)
    LinearLayout speakLanguageLayout;
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
    private List<AiEntity> beans;
    private LinearLayoutManager mLinearLayoutManager;
    private SpeechRecognizer recognizer;
    public RcAiChatAdapter mAdapter;
    private SharedPreferences sp;
    private KaiPinAdUIModelCustom mKaiPinAdUIModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_chat_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initData();
        mKaiPinAdUIModel = new KaiPinAdUIModelCustom(this, adSource, adImg, adLayout,
                contentLv, numberProgressBar, progressTv);
        mKaiPinAdUIModel.setOnFinishListener(new OnFinishListener() {
            @Override
            public void OnFinish() {
                sayHi();
            }
        });
    }

    private void initData() {
        setActionBarTitle(getResources().getString(R.string.title_ai_chat));
        sp = Settings.getSharedPreferences(this);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        beans = new ArrayList<AiEntity>();
        beans.addAll(DataBaseUtil.getInstance().getAiEntityList(AiUtil.Ai_Acobot));
        mAdapter = new RcAiChatAdapter(this, beans, mProgressbar);
        mAdapter.setItems(beans);
        mLinearLayoutManager = new LinearLayoutManager(this);
        if (beans.size() > 4) {
            mLinearLayoutManager.setStackFromEnd(true);
        }
        contentLv.setLayoutManager(mLinearLayoutManager);
        contentLv.setAdapter(mAdapter);
        setSpeakLanguageTv();
        if (sp.getBoolean(KeyUtil.IsAiChatPlayVoice, true)) {
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

    private void sayHi() {
        String sayHi = "say hello";
        if (beans.size() > 0) {
            AiEntity mAiEntity = beans.get(beans.size() - 1);
            if (!sayHi.equals(mAiEntity.getContent())) {
                requestData(sayHi);
            }
        } else {
            requestData(sayHi);
        }
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        if(beans.size() > 0){
            List<AiEntity> list = DataBaseUtil.getInstance().getAiEntityList(beans.get(0).getId(), AiUtil.Ai_Acobot);
            if (list.size() > 0) {
                beans.addAll(0, list);
                mAdapter.notifyDataSetChanged();
                contentLv.scrollToPosition(list.size());
            }
        }
        onSwipeRefreshLayoutFinish();
    }

    @OnClick({R.id.volume_btn, R.id.submit_btn_cover, R.id.input_type_layout,
            R.id.voice_btn_cover, R.id.speak_language_layout,R.id.delete_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.volume_btn:
                boolean isPlay = !sp.getBoolean(KeyUtil.IsAiChatPlayVoice, true);
                Settings.saveSharedPreferences(sp,
                        KeyUtil.IsAiChatPlayVoice,
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
                showIatDialog();
                break;
            case R.id.speak_language_layout:
                changeSpeakLanguage();
                break;
            case R.id.delete_btn:
                clear_all();
                break;
        }
    }

    private void clear_all(){
        beans.clear();
        mAdapter.notifyDataSetChanged();
        DataBaseUtil.getInstance().deleteAiEntity(AiUtil.Ai_Acobot);
    }

    private void submit() {
        if (!TextUtils.isEmpty(inputEt.getText().toString().trim())) {
            AiEntity mAiEntity = new AiEntity();
            mAiEntity.setRole(AiUtil.Role_User);
            mAiEntity.setContent_video_id(String.valueOf(System.currentTimeMillis()));
            mAiEntity.setContent(inputEt.getText().toString().trim());
            mAiEntity.setContent_type(AiUtil.Content_Type_Text);
            mAiEntity.setEntity_type(AiUtil.Entity_Type_Chat);
            mAiEntity.setAi_type(AiUtil.Ai_Acobot);
            mAdapter.addEntity(beans.size(), mAiEntity);
            contentLv.scrollToPosition(beans.size() - 1);
            requestData(inputEt.getText().toString().trim());
            inputEt.setText("");
            DataBaseUtil.getInstance().insert(mAiEntity);
        }
    }

    /**
     * 显示转写对话框.
     */
    public void showIatDialog() {
        Settings.verifyStoragePermissions(this, Settings.PERMISSIONS_RECORD_AUDIO);
        if (!recognizer.isListening()) {
            recordLayout.setVisibility(View.VISIBLE);
            inputEt.setText("");
            voiceBtn.setText(this.getResources().getText(R.string.click_and_finish));
            XFUtil.showSpeechRecognizer(this, PlayUtil.getSP(), recognizer, recognizerListener,
                    PlayUtil.getSP().getString(KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineEN));
        } else {
            finishRecord();
            recognizer.stopListening();
            showProgressbar();
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        recordLayout.setVisibility(View.GONE);
        recordAnimImg.setBackgroundResource(R.drawable.speak_voice_1);
        voiceBtn.setText(this.getResources().getText(R.string.click_and_speak));
    }

    private void changeSpeakLanguage() {
        if (PlayUtil.getSP().getString(KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineEN).equals(XFUtil.VoiceEngineMD)) {
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(this, this.getResources().getString(R.string.speak_english));
        } else {
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineMD);
            ToastUtil.diaplayMesShort(this, this.getResources().getString(R.string.speak_chinese));
        }
        setSpeakLanguageTv();
        AVAnalytics.onEvent(this, "tab3_ai_changelan");
    }

    private void setSpeakLanguageTv() {
        speakLanguageTv.setText(XFUtil.getVoiceEngineText(sp.getString(KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineEN)));
    }

    private void changeInputType() {
        if (keybordLayout.isShown()) {
            showMicLayout();
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsAiChatShowKeybordLayout, false);
            hideIME(inputEt);
        } else {
            showKeybordLayout();
            Settings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.IsAiChatShowKeybordLayout, true);
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
        String url = Settings.AiBrainUrl + Settings.getUUID(this) + "&msg=" + msg;
        LanguagehelperHttpClient.get(url, new UICallback(this) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    LogUtil.DefalutLog(responseString);
                    if (JsonParser.isJson(responseString)) {
                        AiResult mAiResult = JSON.parseObject(responseString, AiResult.class);
                        addAiResult(mAiResult);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(AiChatActivity.this, AiChatActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onSwipeRefreshLayoutFinish();
                hideProgressbar();
            }
        });
    }

    private void addAiResult(AiResult mAiResult) {
        if (mAiResult != null) {
            AiEntity mAiEntity = parseResult(mAiResult.getCnt());
            mAdapter.addEntity(beans.size(), mAiEntity);
            contentLv.scrollToPosition(beans.size() - 1);
            if (sp.getBoolean(KeyUtil.IsAiChatPlayVoice, true)) {
                playVideo(mAiEntity);
            }
            DataBaseUtil.getInstance().insert(mAiEntity);
        }
    }

    private AiEntity parseResult(String result) {
        AiEntity mAiEntity = new AiEntity();
        mAiEntity.setRole(AiUtil.Role_Machine);
        mAiEntity.setEntity_type(AiUtil.Entity_Type_Chat);
        mAiEntity.setAi_type(AiUtil.Ai_Acobot);
        mAiEntity.setContent_video_id(String.valueOf(System.currentTimeMillis()));
        if (result.contains("<a href=")) {
            Document doc = Jsoup.parse(result);
            Element element = doc.select("a").first();
            mAiEntity.setContent(element.text());
            mAiEntity.setContent_type(AiUtil.Content_Type_Link);
            mAiEntity.setLink(element.attr("href"));
        } else {
            mAiEntity.setContent(result);
            mAiEntity.setContent_type(AiUtil.Content_Type_Text);
        }
        return mAiEntity;
    }

    public void playVideo(final AiEntity mAiEntity) {
        String filepath = "";
        String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
        if (TextUtils.isEmpty(mAiEntity.getContent_video_id())) {
            mAiEntity.setContent_video_id(String.valueOf(System.currentTimeMillis()));
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
                            ToastUtil.diaplayMesShort(AiChatActivity.this, arg0.getErrorDescription());
                        }
                        DataBaseUtil.getInstance().update(mAiEntity);
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
            ToastUtil.diaplayMesShort(AiChatActivity.this, err.getErrorDescription());
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

}
