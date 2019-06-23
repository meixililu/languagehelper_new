package com.messi.languagehelper.http;

import android.app.Activity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class UICallback implements Callback {
	
	private Activity context;
	private String responseString;
	
	public UICallback(Activity context){
		this.context = context;
	}

	@Override
	public void onFailure(Call call, IOException e) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onFinished();
				onFailured();
			}
		});
	}

	@Override
	public void onResponse(Call call,final Response mResponse) throws IOException {
		if (mResponse != null && mResponse.isSuccessful()){
			responseString = mResponse.body().string();
			if(context != null){
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onFinished();
						onResponsed(responseString);
					}
				});
			}
		}else {
			if(context != null){
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						onFinished();
						onFailured();
					}
				});
			}
		}
	}
	
	public void onFailured() {}
	public void onFinished() {}
	public void onResponsed(String responseString) {}


}
