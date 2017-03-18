package com.messi.languagehelper.views;

import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.DictionaryFragment;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dialog.TranslateResultDialog;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ProgressBar;

public class TouchableSpan extends ClickableSpan {// extend ClickableSpan

	private String word;
	private Context context;
	private ProgressBar mProgressbar;
	private FragmentProgressbarListener mProgressbarListener;

	public TouchableSpan(Context context, ProgressBar mProgressbar, String string) {
		super();
		word = string;
		this.mProgressbar = mProgressbar;
		this.context = context;
	}
	
	public TouchableSpan(Context context, FragmentProgressbarListener mProgressbarListener, String string) {
		super();
		word = string;
		this.mProgressbarListener = mProgressbarListener;
		this.context = context;
	}

	public void onClick(View tv) {
		LogUtil.DefalutLog(word);
		RequestShowapiAsyncTask();
	}

	public void updateDrawState(TextPaint ds) {
		ds.setUnderlineText(false); // set to false to remove underline
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(mProgressbar != null){
				mProgressbar.setVisibility(View.GONE);
			}
			if(mProgressbarListener != null){
				mProgressbarListener.hideProgressbar();
			}
			if(msg.what == 1){
				setData();
			}else{
				showToast(context.getResources().getString(R.string.network_error));
			}
		}
	};
	
	private void RequestShowapiAsyncTask(){
		if(mProgressbar != null){
			mProgressbar.setVisibility(View.VISIBLE);
		}
		if(mProgressbarListener != null){
			mProgressbarListener.showProgressbar();
		}
		Settings.q = word;
		TranslateUtil.Translate_init(context, mHandler);
	}
	
	private void setData(){
		Dictionary bean = (Dictionary) WXEntryActivity.dataMap.get(KeyUtil.DataMapKey);
		WXEntryActivity.dataMap.clear();
		TranslateResultDialog dialog = new TranslateResultDialog(context, bean);
		dialog.createDialog();
		dialog.show();
	}
	
	private void showToast(String toastString) {
		ToastUtil.diaplayMesShort(context, toastString);
	}
	
}
