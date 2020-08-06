package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.adapter.RcAiChatAdapter;
import com.messi.languagehelper.bean.AiResult;
import com.messi.languagehelper.box.AiEntity;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.http.LanguagehelperHttpClient;
import com.messi.languagehelper.http.UICallback;
import com.messi.languagehelper.impl.OnFinishListener;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.AiUtil;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KViewUtil;
import com.messi.languagehelper.util.KaiPinAdUIModelCustom;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MD5;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.PlayUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Setings;
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
import cn.leancloud.json.JSON;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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
    @BindView(R.id.splash_container)
    FrameLayout splash_container;
    @BindView(R.id.skip_view)
    TextView skip_view;
    private List<AiEntity> beans;
    private LinearLayoutManager mLinearLayoutManager;
    private SpeechRecognizer recognizer;
    public RcAiChatAdapter mAdapter;
    private SharedPreferences sp;
    private KaiPinAdUIModelCustom mKaiPinAdUIModel;
    private boolean isSayHello;
    private SpeechSynthesizer mSpeechSynthesizer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_chat_activity);
        ButterKnife.bind(this);
        initSwipeRefresh();
        initData();
        mKaiPinAdUIModel = new KaiPinAdUIModelCustom(this, adSource, adImg, adLayout,
                contentLv, numberProgressBar, progressTv, splash_container, skip_view);
        mKaiPinAdUIModel.setOnFinishListener(new OnFinishListener() {
            @Override
            public void OnFinish() {
                sayHi();
            }
        });
    }

    private void initData() {
        setActionBarTitle(getResources().getString(R.string.title_ai_chat));
        sp = Setings.getSharedPreferences(this);
        recognizer = SpeechRecognizer.createRecognizer(this.getApplicationContext(), null);
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this.getApplicationContext(), null);
        beans = new ArrayList<AiEntity>();
        beans.addAll(BoxHelper.getAiEntityList(AiUtil.Ai_Acobot));
        mAdapter = new RcAiChatAdapter(this, beans);
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
        if(!isSayHello){
            isSayHello = true;
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

    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        if(beans.size() > 0){
            List<AiEntity> list = BoxHelper.getAiEntityList(beans.get(0).getId(), AiUtil.Ai_Acobot);
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
                Setings.saveSharedPreferences(sp,
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
                AiChatActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
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
        BoxHelper.deleteAiEntity(AiUtil.Ai_Acobot);
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
            BoxHelper.insertOrUpdate(mAiEntity);
        }
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void showIatDialog() {
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
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineEN);
            ToastUtil.diaplayMesShort(this, this.getResources().getString(R.string.speak_english));
        } else {
            Setings.saveSharedPreferences(PlayUtil.getSP(), KeyUtil.AiChatUserSelectLanguage, XFUtil.VoiceEngineMD);
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
        String url = Setings.AiBrainUrl + Setings.getDeviceID(this) + "&msg=" + msg;
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
            BoxHelper.insertOrUpdate(mAiEntity);
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
        MyPlayer.getInstance(this).start(mAiEntity.getContent());
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
            KViewUtil.INSTANCE.showRecordImgAnimation(volume, recordAnimImg);
        }

    };

    @Override
    public void onBackPressed() {
        if (inputEt != null) {
            hideIME(inputEt);
        }
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AiChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSpeechSynthesizer != null){
            mSpeechSynthesizer.stopSpeaking();
            mSpeechSynthesizer.destroy();
            mSpeechSynthesizer = null;
        }
        MyPlayer.getInstance(this).onDestroy();
    }
}
