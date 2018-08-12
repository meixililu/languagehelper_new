package com.messi.languagehelper.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class BgCallback implements Callback {

	private String responseString;

	@Override
	public void onFailure(Call call, IOException e) {
		onFinished();
		onFailured();
	}

	@Override
	public void onResponse(Call call,final Response mResponse) throws IOException {
		if (mResponse != null && mResponse.isSuccessful()){
			responseString = mResponse.body().string();
		}
		onFinished();
		onResponsed(responseString);
	}
	
	public void onFailured() {}
	public void onFinished() {}
	public void onResponsed(String responseString) {}


}
