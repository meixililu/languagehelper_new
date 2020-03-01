package com.messi.languagehelper.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Dictionary;
import com.messi.languagehelper.impl.DicHelperListener;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.DictionaryHelper;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.XFUtil;

public class TranslateResultDialog implements DicHelperListener {
	
	private Activity context;
	private Dictionary bean;
	private SpeechSynthesizer mSpeechSynthesizer;
	private SharedPreferences mSharedPreferences;
	private Thread mThread;
	private MyThread mMyThread;
	private AlertDialog dialog;

	public TranslateResultDialog(Activity context) {
	    this.context = context;
	}

	/**
	 * 更改TextView的提示内容
	 * @param context
	 */
	public TranslateResultDialog(Activity context, Dictionary bean) {
		this.context = context;
		this.bean = bean;
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(context, null);
	}

	public void createDialog() {
		if(context instanceof Activity){
			if(!((Activity)context).isFinishing()){
				View view = LayoutInflater.from(context).inflate(R.layout.dialog_translate_result,null);
				dialog = new AlertDialog.Builder(context).create();
				dialog.setView(view);
				dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialogInterface) {
						if(mSpeechSynthesizer != null && mSpeechSynthesizer.isSpeaking()){
							mSpeechSynthesizer.stopSpeaking();
							mSpeechSynthesizer = null;
						}
					}
				});
				mMyThread = new MyThread();
				LinearLayout dic_result_layout = (LinearLayout) view.findViewById(R.id.dic_result_layout);
				DictionaryHelper.addDicContentForDialog(context, dic_result_layout, bean, this);
			}
		}
	}

	public void show(){
		if(context instanceof Activity){
			if(!((Activity)context).isFinishing()){
				dialog.show();
			}
		}
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
								BoxHelper.update(mObject);
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


}
