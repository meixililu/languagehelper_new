package com.messi.languagehelper.views;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ProgressBar;

import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.dialog.TranslateResultDialog;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.TranslateUtil;
import com.youdao.sdk.ydtranslate.Translate;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TouchableSpan extends ClickableSpan {// extend ClickableSpan

	private String word;
	private Activity context;
	private ProgressBar mProgressbar;
	private FragmentProgressbarListener mProgressbarListener;

	public TouchableSpan(Activity context, ProgressBar mProgressbar, String string) {
		super();
		word = string;
		this.mProgressbar = mProgressbar;
		this.context = context;
	}
	
	public TouchableSpan(Activity context, FragmentProgressbarListener mProgressbarListener, String string) {
		super();
		word = string;
		this.mProgressbarListener = mProgressbarListener;
		this.context = context;
	}

	public void onClick(View tv) {
		LogUtil.DefalutLog(word);
		translateController();
	}

	public void updateDrawState(TextPaint ds) {
		ds.setUnderlineText(false); // set to false to remove underline
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			hideProgressbar();
			if(msg.what == 1){
				setData();
			}else{
				showToast(context.getResources().getString(R.string.network_error));
			}
		}
	};

	private void translateController(){
		Setings.q = word;
		if(NetworkUtil.isNetworkConnected(context)){
			LogUtil.DefalutLog("online");
			RequestShowapiAsyncTask();
		}else {
			LogUtil.DefalutLog("offline");
			translateOffline();
		}
	}

	private void translateOffline(){
		showProgressbar();
		Observable.create(new ObservableOnSubscribe<Translate>() {
			@Override
			public void subscribe(ObservableEmitter<Translate> e) throws Exception {
				TranslateUtil.offlineTranslate(e);
			}
		})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<Translate>() {
					@Override
					public void onSubscribe(Disposable d) {
					}
					@Override
					public void onNext(Translate translate) {
						parseOfflineData(translate);
					}
					@Override
					public void onError(Throwable e) {
						onComplete();
					}
					@Override
					public void onComplete() {
						hideProgressbar();
					}
				});
	}

	private void parseOfflineData(Translate translate){
		if(translate != null){
			if(translate.getErrorCode() == 0){
				StringBuilder sb = new StringBuilder();
				TranslateUtil.addSymbol(translate,sb);
				for(String tran : translate.getTranslations()){
					sb.append(tran);
					sb.append("\n");
				}
				Dictionary mDictionaryBean = new Dictionary();
				boolean isEnglish = StringUtils.isEnglish(Setings.q);
				if(isEnglish){
					mDictionaryBean.setFrom("en");
					mDictionaryBean.setTo("zh");
				}else{
					mDictionaryBean.setFrom("zh");
					mDictionaryBean.setTo("en");
				}
				mDictionaryBean.setWord_name(Setings.q);
				mDictionaryBean.setResult(sb.substring(0, sb.lastIndexOf("\n")));
				DataBaseUtil.getInstance().insert(mDictionaryBean);
				showDialog(mDictionaryBean);
			}
		}else{
			showToast("没找到离线词典，请到更多页面下载！");
		}
	}
	
	private void RequestShowapiAsyncTask(){
		showProgressbar();
		TranslateUtil.Translate_init(context, mHandler);
	}
	
	private void setData(){
		Dictionary bean = (Dictionary) Setings.dataMap.get(KeyUtil.DataMapKey);
		Setings.dataMap.clear();
		showDialog(bean);
	}

	private void showDialog(Dictionary bean){
		try {
			TranslateResultDialog dialog = new TranslateResultDialog(context, bean);
			dialog.createDialog();
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showProgressbar(){
		if(mProgressbar != null){
			mProgressbar.setVisibility(View.VISIBLE);
		}
		if(mProgressbarListener != null){
			mProgressbarListener.showProgressbar();
		}
	}

	private void hideProgressbar(){
		if(mProgressbar != null){
			mProgressbar.setVisibility(View.GONE);
		}
		if(mProgressbarListener != null){
			mProgressbarListener.hideProgressbar();
		}
	}
	
	private void showToast(String toastString) {
		ToastUtil.diaplayMesShort(context, toastString);
	}
	
}
