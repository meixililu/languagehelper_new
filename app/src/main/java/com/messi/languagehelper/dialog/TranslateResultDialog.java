package com.messi.languagehelper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.jeremyliao.liveeventbus.LiveEventBus;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.Record;
import com.messi.languagehelper.databinding.DialogTranslateResultBinding;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.MyPlayer;
import com.messi.languagehelper.util.NetworkUtil;
import com.messi.languagehelper.util.Setings;
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

public class TranslateResultDialog extends Dialog {

	private DialogTranslateResultBinding binding;
	private Context context;
	private String word;
	private Record mBean;

	public TranslateResultDialog(Context context, String word) {
		super(context, R.style.mydialog);
	    this.context = context;
	    this.word = word;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = DialogTranslateResultBinding.inflate(LayoutInflater.from(context));
		setContentView(binding.getRoot());
		binding.dicTitle.setText(word);
		binding.desTv.setText("Loading...");
		binding.collectedCb.setOnClickListener(view -> {
				updateCollectedStatus();
		});
		binding.titleLayout.setOnClickListener(view -> {
			play();
		});
		translateController();
	}

	private void play(){
		if (mBean != null && !TextUtils.isEmpty(mBean.getPh_en_mp3())) {
			MyPlayer.getInstance(context).start(word,mBean.getPh_en_mp3());
		} else {
			MyPlayer.getInstance(context).start(word);
		}
	}

	private void updateCollectedStatus(){
		if(mBean != null){
			if (mBean.getIscollected().equals("0")) {
				mBean.setIscollected("1");
				binding.collectedCb.setChecked(true);
				ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_success));
				TranslateUtil.addToNewword(mBean);
			} else {
				mBean.setIscollected("0");
				binding.collectedCb.setChecked(false);
				ToastUtil.diaplayMesShort(context,context.getResources().getString(R.string.favorite_cancle));
			}
			BoxHelper.update(mBean);
		}
	}
	private void translateController(){
		Setings.q = word;
		if(NetworkUtil.isNetworkConnected(getContext())){
			LogUtil.DefalutLog("online");
			RequestShowapiAsyncTask();
		}else {
			LogUtil.DefalutLog("offline");
			translateOffline();
		}
	}

	private void translateOffline(){
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
				mBean = new Record();
				mBean.setChinese(Setings.q);
				mBean.setEnglish(sb.substring(0, sb.lastIndexOf("\n")));
				binding.desTv.setText(mBean.getEnglish());
				BoxHelper.insert(mBean);
				LiveEventBus.get(KeyUtil.TranAndDicRefreshEvent).post("reload");
			}
		}else{
			ToastUtil.diaplayMesShort(context,"没找到离线词典，请到设置页面下载！");
		}
	}

	private void RequestShowapiAsyncTask(){
		TranslateUtil.Translate(mrecord -> onResult(mrecord));
	}

	private void onResult(Record mRecord){
		mBean = mRecord;
		if(mRecord == null){
			ToastUtil.diaplayMesShort(context,getContext().getResources().getString(R.string.network_error));
		}else {
			binding.desTv.setText(mRecord.getEnglish());
			BoxHelper.insert(mRecord);
			LiveEventBus.get(KeyUtil.TranAndDicRefreshEvent).post("reload");
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyPlayer.getInstance(context).onDestroy();
	}
}
