package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.util.AVAnalytics;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class KSearchActivity extends BaseActivity {

    @BindView(R.id.input_et)
    EditText input_et;
    @BindView(R.id.voice_btn)
    Button voice_btn;
    @BindView(R.id.input_layout)
    LinearLayout input_layout;
    @BindView(R.id.speak_round_layout)
    LinearLayout speak_round_layout;
    @BindView(R.id.record_anim_img)
    ImageView record_anim_img;
    @BindView(R.id.search_novel)
    TextView search_novel;
    @BindView(R.id.search_internet)
    TextView search_internet;
    @BindView(R.id.search_caricature)
    TextView search_caricature;
    @BindView(R.id.record_layout)
    LinearLayout record_layout;
    @BindView(R.id.search_btn)
    FrameLayout search_btn;

    private SpeechRecognizer recognizer;
    private SharedPreferences sp;
    private String SearchUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ksearch_activity);
        ButterKnife.bind(this);
        changeStatusBarTextColor(true);
        setStatusbarColor(R.color.white);
        initData();
    }

    private void initData() {
        sp = Setings.getSharedPreferences(this);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        setSelectedItem(sp.getInt(KeyUtil.KSearchSelected, 0));
        input_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                showResult();
            }
            return false;
            }
        });
    }

    private void setSelectedItem(int position) {
        resetItems();
        if (position == 0) {
            SearchUrl = Setings.UCSearchUrl;
            search_internet.setBackgroundResource(R.drawable.bg_btn_orange_circle);
        } else if (position == 1) {
            SearchUrl = Setings.NovelSearchUrl;
            search_novel.setBackgroundResource(R.drawable.bg_btn_orange_circle);
        } else {
            SearchUrl = Setings.CaricatureSearchUrl;
            search_caricature.setBackgroundResource(R.drawable.bg_btn_orange_circle);
        }
        Setings.saveSharedPreferences(sp, KeyUtil.KSearchSelected, position);
    }

    private void resetItems() {
        search_internet.setBackgroundResource(R.drawable.bg_btn_gray_circle);
        search_novel.setBackgroundResource(R.drawable.bg_btn_gray_circle);
        search_caricature.setBackgroundResource(R.drawable.bg_btn_gray_circle);
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
            ToastUtil.diaplayMesShort(KSearchActivity.this, err.getErrorDescription());
        }

        @Override
        public void onEndOfSpeech() {
            LogUtil.DefalutLog("onEndOfSpeech");
            finishRecord();
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.DefalutLog("onResult---getResultString:" + results.getResultString());
            String text = JsonParser.parseIatResult(results.getResultString());
            input_et.append(text.toLowerCase());
            input_et.setSelection(input_et.length());
            if (isLast) {
                finishRecord();
                showResult();
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

        @Override
        public void onVolumeChanged(int volume, byte[] arg1) {
            if (volume < 4) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
            } else if (volume < 8) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_2);
            } else if (volume < 12) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_3);
            } else if (volume < 16) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_4);
            } else if (volume < 20) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_5);
            } else if (volume < 24) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_6);
            } else if (volume < 31) {
                record_anim_img.setBackgroundResource(R.drawable.speak_voice_7);
            }
        }
    };

    private void showResult() {
        String question = input_et.getText().toString().trim();
        if (!TextUtils.isEmpty(question) && question.length() > 0) {
            String last = question.substring(question.length() - 1);
            if (",.!;:'，。！‘；：".contains(last)) {
                question = question.substring(0, question.length() - 1);
            }
            input_et.setText(question);
            String url = SearchUrl.replace("{0}",question);
            toResult(url);
        }
    }

    private void toResult(String url) {
        Intent intent = new Intent(this, WebViewWithMicActivity.class);
        intent.putExtra(KeyUtil.URL, url);
        if(sp.getInt(KeyUtil.KSearchSelected, 0) == 0){
            intent.putExtra(KeyUtil.SearchUrl, Setings.UCSearchUrl);
        }else if(sp.getInt(KeyUtil.KSearchSelected, 0) == 1){
            intent.putExtra(KeyUtil.isHideMic,true);
            intent.putExtra(KeyUtil.FilterName,"找小说");
            intent.putExtra(KeyUtil.IsNeedGetFilter, true);
            intent.putExtra(KeyUtil.SearchUrl, Setings.NovelSearchUrl);
        }else {
            intent.putExtra(KeyUtil.isHideMic,true);
            intent.putExtra(KeyUtil.FilterName,"找漫画");
            intent.putExtra(KeyUtil.IsNeedGetFilter, true);
            intent.putExtra(KeyUtil.SearchUrl, Setings.CaricatureSearchUrl);
        }
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        intent.putExtra(KeyUtil.IsReedPullDownRefresh, false);
        startActivity(intent);
    }

    /**
     * 显示转写对话框.
     */
    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void showIatDialog() {
        if (recognizer != null) {
            if (!recognizer.isListening()) {
                record_layout.setVisibility(View.VISIBLE);
                input_et.setText("");
                voice_btn.setBackgroundColor(this.getResources().getColor(R.color.none));
                voice_btn.setText(this.getResources().getString(R.string.finish));
                speak_round_layout.setBackgroundResource(R.drawable.round_light_blue_bgl);
                XFUtil.showSpeechRecognizer(this, sp, recognizer,
                        recognizerListener, XFUtil.VoiceEngineMD);
            } else {
                recognizer.stopListening();
                finishRecord();
            }
        }
    }

    /**
     * finish record
     */
    private void finishRecord() {
        record_layout.setVisibility(View.GONE);
        record_anim_img.setBackgroundResource(R.drawable.speak_voice_1);
        voice_btn.setText("");
        voice_btn.setBackgroundResource(R.drawable.ic_voice_padded_normal);
        speak_round_layout.setBackgroundResource(R.drawable.round_gray_bgl_old);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (recognizer != null) {
            recognizer.destroy();
            recognizer = null;
        }
    }

    @OnClick({R.id.search_novel, R.id.search_internet, R.id.search_caricature,
            R.id.speak_round_layout,R.id.search_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.search_novel:
                setSelectedItem(1);
                AVAnalytics.onEvent(KSearchActivity.this, "ksearch_novel");
                break;
            case R.id.search_internet:
                setSelectedItem(0);
                AVAnalytics.onEvent(KSearchActivity.this, "ksearch_internet");
                break;
            case R.id.search_caricature:
                setSelectedItem(2);
                AVAnalytics.onEvent(KSearchActivity.this, "ksearch_caricature");
                break;
            case R.id.speak_round_layout:
                KSearchActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
                AVAnalytics.onEvent(KSearchActivity.this, "ksearch_speak_btn");
                break;
            case R.id.search_btn:
                showResult();
                AVAnalytics.onEvent(KSearchActivity.this, "ksearch_search_btn");
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        KSearchActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
