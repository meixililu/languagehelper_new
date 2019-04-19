package com.messi.languagehelper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.util.ScreenUtil;

public class OCRDialog extends Dialog {

	private PopViewItemOnclickListener listener;
	private Context context;
	private String[] tempText;
	private boolean isHideBaiDuTag;

	public void setListener(PopViewItemOnclickListener listener) {
		this.listener = listener;
	}

	public OCRDialog(Context context, int theme) {
	    super(context, theme);
	    this.context = context;
	}

	public OCRDialog(Context context) {
	    super(context);
	    this.context = context;
	}

	/**
	 * 更改TextView的提示内容
	 * @param context
	 * @param theme
	 * @param tempText
	 */
	public OCRDialog(Context context, String[] tempText) {
		super(context, R.style.mydialog);
		this.context = context;
		this.tempText = tempText;
	}

	public OCRDialog(Context context, String[] tempText, boolean isHideBaiDuTag) {
		super(context, R.style.mydialog);
		this.context = context;
		this.tempText = tempText;
		this.isHideBaiDuTag = isHideBaiDuTag;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ocr_pop_dialog);
	    FrameLayout text1Cover = (FrameLayout) findViewById(R.id.select_popwindow_text1_cover);
	    FrameLayout text2Cover = (FrameLayout) findViewById(R.id.select_popwindow_text2_cover);
		LinearLayout baidu_tag = (LinearLayout) findViewById(R.id.baidu_tag);
	    TextView text1 = (TextView) findViewById(R.id.select_popwindow_text1);
	    TextView text2 = (TextView) findViewById(R.id.select_popwindow_text2);
	    text1Cover.setOnClickListener(onClickListener);
	    text2Cover.setOnClickListener(onClickListener);
		if(isHideBaiDuTag){
			baidu_tag.setVisibility(View.GONE);
		}
		if(tempText != null){
			text1.setText(tempText[0]);
			text2.setText(tempText[1]);
		}
	}
	
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.select_popwindow_text1_cover:
				 if(listener != null){
					 listener.onFirstClick(v);
				 }
				 OCRDialog.this.dismiss();
				 break;
			case R.id.select_popwindow_text2_cover:
				 if(listener != null){
					 listener.onSecondClick(v);
				 }					
				 OCRDialog.this.dismiss();
				 break;
			}
		}
	};
	
	public void setPopViewPosition(){
		Window win = this.getWindow();
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.gravity = Gravity.TOP|Gravity.RIGHT;
		params.x = ScreenUtil.dip2px(context,10f);
		params.y = ScreenUtil.dip2px(context,45f);
		win.setAttributes(params);
		this.setCanceledOnTouchOutside(true);
	}
	
	public interface PopViewItemOnclickListener{
		public void onFirstClick(View v);
		public void onSecondClick(View v);
	}
}
