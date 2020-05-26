package com.messi.languagehelper.views;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.messi.languagehelper.dialog.TranslateResultDialog;

public class TouchableSpan extends ClickableSpan {

	private String word;
	private Context context;

	public TouchableSpan(Context context, String string) {
		super();
		word = string;
		this.context = context;
	}

	public void onClick(View tv) {
		showDialog();
	}

	public void updateDrawState(TextPaint ds) {
		// set to false to remove underline
		ds.setUnderlineText(false);
	}

	private void showDialog(){
		try {
			TranslateResultDialog dialog = new TranslateResultDialog(context, word);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




}
