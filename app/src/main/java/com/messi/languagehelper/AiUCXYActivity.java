package com.messi.languagehelper;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.messi.languagehelper.util.JsonParser;
import com.messi.languagehelper.util.KaiPinAdUIModelCustom;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;
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
public class AiUCXYActivity extends BaseActivity {

    @BindView(R.id.input_et)
    AppCompatEditText inputEt;
    @BindView(R.id.submit_btn_cover)
    CardView submitBtn;
    @BindView(R.id.webview_layout)
    WebView mWebView;
    @BindView(R.id.record_anim_img)
    ImageView recordAnimImg;
    @BindView(R.id.record_layout)
    LinearLayout recordLayout;
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
    @BindView(R.id.splash_container)
    FrameLayout splash_container;
    @BindView(R.id.skip_view)
    TextView skip_view;
    private SpeechRecognizer recognizer;
    private SharedPreferences sp;
    private KaiPinAdUIModelCustom mKaiPinAdUIModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ai_ucxy_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.white);
        changeStatusBarTextColor(true);
        initData();
        mKaiPinAdUIModel = new KaiPinAdUIModelCustom(this, adSource, adImg, adLayout,
                mWebView, numberProgressBar, progressTv, splash_container, skip_view);
    }

    private void initData() {
        sp = Setings.getSharedPreferences(this);
        recognizer = SpeechRecognizer.createRecognizer(this, null);
        if (sp.getBoolean(KeyUtil.IsAiChatShowKeybordLayout, true)) {
            showKeybordLayout();
        } else {
            showMicLayout();
        }
        inputEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_GO) {
                    submit();
                }
                return false;
            }
        });
        keybordLayout.requestFocus();
        mWebView.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        mWebView.getSettings().setJavaScriptEnabled(true);//如果访问的页面中有Javascript，则webview必须设置支持Javascript。
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAllowFileAccess(true); //设置可以访问文件
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setBlockNetworkImage(false);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LogUtil.DefalutLog("WebViewClient:onPageStarted");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LogUtil.DefalutLog("failingUrl:"+failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LogUtil.DefalutLog("WebViewClient:onPageFinished");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.DefalutLog("shouldOverrideUrlLoading:"+url);
                showNewUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                LogUtil.DefalutLog("shouldInterceptRequest:"+url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AiUCXYActivity.this);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        mWebView.loadUrl(Setings.UCAI);
    }

    @OnClick({R.id.submit_btn_cover, R.id.input_type_layout,
            R.id.voice_btn_cover})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit_btn_cover:
                submit();
                hideIME(inputEt);
                break;
            case R.id.input_type_layout:
                changeInputType();
                break;
            case R.id.voice_btn_cover:
                AiUCXYActivityPermissionsDispatcher.showIatDialogWithPermissionCheck(this);
                break;

        }
    }

    private void submit() {
        String input = inputEt.getText().toString().trim();
        if (!TextUtils.isEmpty(input)) {
            String last = input.substring(input.length() - 1);
            if (",.!;:'，。！‘；：".contains(last)) {
                input = input.substring(0, input.length() - 1);
            }
            inputEt.setText("");
            mWebView.loadUrl(Setings.UCAI+input);
        }
    }

    private void showNewUrl(String url){
        if(url.contains("wh10342")){
            url = url.replace("wh10342","wm845578");
        }
        if(url.contains("m.sm.cn")){
            url = url.replace("m.sm.cn","yz.m.sm.cn");
        }
        LogUtil.DefalutLog("showNewUrl:"+url);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(KeyUtil.URL, url);
        intent.putExtra(KeyUtil.IsHideToolbar, true);
        intent.putExtra(KeyUtil.IsReedPullDownRefresh, false);
        startActivity(intent);
    }

    @NeedsPermission(Manifest.permission.RECORD_AUDIO)
    public void showIatDialog() {
        if (!recognizer.isListening()) {
            recordLayout.setVisibility(View.VISIBLE);
            inputEt.setText("");
            voiceBtn.setText(this.getResources().getText(R.string.click_and_finish));
            XFUtil.showSpeechRecognizer(this,
                    sp,
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
            Setings.saveSharedPreferences(sp, KeyUtil.IsAiChatShowKeybordLayout, false);
            hideIME(inputEt);
        } else {
            showKeybordLayout();
            Setings.saveSharedPreferences(sp, KeyUtil.IsAiChatShowKeybordLayout, true);
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

    RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            LogUtil.DefalutLog("onBeginOfSpeech");
        }

        @Override
        public void onError(SpeechError err) {
            LogUtil.DefalutLog("onError:" + err.getErrorDescription());
            finishRecord();
            ToastUtil.diaplayMesShort(AiUCXYActivity.this, err.getErrorDescription());
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
    protected void onDestroy() {
        super.onDestroy();
        ViewUtil.destroyWebView(mWebView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AiUCXYActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
