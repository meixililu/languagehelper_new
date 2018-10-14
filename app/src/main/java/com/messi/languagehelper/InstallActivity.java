package com.messi.languagehelper;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.messi.languagehelper.util.AppUpdateUtil;
import com.messi.languagehelper.util.KeyUtil;

public class InstallActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(getIntent() != null){
			String action = getIntent().getStringExtra(KeyUtil.Type);
			if(!TextUtils.isEmpty(action)){
				if(action.equals("install")){
					AppUpdateUtil.checkUpdate(this);
				}
			}
		}
		finish();
	}
	
}
