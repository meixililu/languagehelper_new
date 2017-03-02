package com.messi.languagehelper.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.DicHelperListener;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.XFUtil;

public class TranslateResultDialog extends Dialog implements DicHelperListener {
	
	private Context context;
	private Dictionary bean;
	private SpeechSynthesizer mSpeechSynthesizer;
	private SharedPreferences mSharedPreferences;
	private Thread mThread;
	private MyThread mMyThread;

	public TranslateResultDialog(Context context, int theme) {
	    super(context, theme);
	    this.context = context;
	}

	public TranslateResultDialog(Context context) {
	    super(context);
	    this.context = context;
	}

	/**
	 * 更改TextView的提示内容
	 * @param context
	 * @param theme
	 * @param tempText
	 */
	public TranslateResultDialog(Context context, Dictionary bean) {
		super(context, R.style.mydialog);
		this.context = context;
		this.bean = bean;
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(context, null);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.dialog_translate_result);
	    setFullScreen();
		mMyThread = new MyThread();
	    FrameLayout close_layout = (FrameLayout) findViewById(R.id.close_layout);
		LinearLayout dic_result_layout = (LinearLayout) findViewById(R.id.dic_result_layout);

		DictionaryHelper.addDicContent(context, dic_result_layout, bean, this);
	    close_layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TranslateResultDialog.this.dismiss();
			}
		});
	}

	@Override
	public void playPcm(Dictionary mObject, boolean isPlayResult, String result) {
		play(mObject,isPlayResult,result);
	}
	
	private void play(final Dictionary mObject, boolean isPlayResult, String result){
		if(mSpeechSynthesizer.isSpeaking()){
			mSpeechSynthesizer.stopSpeaking();
		}else{
			String filepath = "";
			String speakContent = "";
			String path = SDCardUtil.getDownloadPath(SDCardUtil.sdPath);
			if(isPlayResult){
				if (TextUtils.isEmpty(mObject.getResultVoiceId())) {
					mObject.setResultVoiceId(System.currentTimeMillis() + 5 + "");
				}
				filepath = path + mObject.getResultVoiceId() + ".pcm";
				speakContent = result;
			}else {
				if (TextUtils.isEmpty(mObject.getQuestionVoiceId())) {
					mObject.setQuestionVoiceId(System.currentTimeMillis() + "");
				}
				filepath = path + mObject.getQuestionVoiceId() + ".pcm";
				speakContent = mObject.getWord_name();
			}
			if (!AudioTrackUtil.isFileExists(filepath)) {
				mSpeechSynthesizer.setParameter(SpeechConstant.TTS_AUDIO_PATH, filepath);
				XFUtil.showSpeechSynthesizer(context,
						mSharedPreferences,
						mSpeechSynthesizer,
						speakContent,
						XFUtil.SpeakerEn,
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
							}
							@Override
							public void onCompleted(SpeechError arg0) {
								DataBaseUtil.getInstance().update(mObject);
							}
							@Override
							public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
							}
							@Override
							public void onEvent(int arg0, int arg1, int arg2,Bundle arg3) {
							}
						});
			}else {
				mMyThread.setDataUri(filepath);
				mThread = AudioTrackUtil.startMyThread(mMyThread);
			}

		}
	}
	
	private void setFullScreen(){
		WindowManager windowManager = this.getWindow().getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		lp.width = display.getWidth() - ScreenUtil.dip2px(context, 10); //设置宽度
		this.getWindow().setAttributes(lp);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		if(mSpeechSynthesizer.isSpeaking()){
			mSpeechSynthesizer.stopSpeaking();
			mSpeechSynthesizer = null;
		}
	}


}
