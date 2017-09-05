package com.messi.languagehelper.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.PopMenuItem;
import com.messi.languagehelper.impl.OnViewClickListener;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.ViewUtil;

import java.util.List;

public class AiItemMenuDialog extends Dialog {

	private OnViewClickListener listener;
	private Context context;
	private List<PopMenuItem> items;
	private LinearLayout popwindow_root;
	private LayoutInflater inflater;

	public void setListener(OnViewClickListener listener) {
		this.listener = listener;
	}

	public AiItemMenuDialog(Context context, int theme) {
	    super(context, theme);
	    this.context = context;
	}

	public AiItemMenuDialog(Context context) {
	    super(context);
	    this.context = context;
	}

	public AiItemMenuDialog(Context context, List<PopMenuItem> items) {
		super(context, R.style.mydialog);
		this.context = context;
		this.items = items;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.popwindow_root);
		popwindow_root = (LinearLayout) findViewById(R.id.popwindow_root);
		inflater = LayoutInflater.from(context);
		initView();
	}

	private void initView(){
		popwindow_root.removeAllViews();
		for (PopMenuItem item : items){
			popwindow_root.addView(getItemView(item),
					ScreenUtil.dip2px(context,200),
					ScreenUtil.dip2px(context,50));
			popwindow_root.addView(ViewUtil.getLine(context));
		}
	}

	private View getItemView(final PopMenuItem item){
		View view = inflater.inflate(R.layout.popwindow_item,null);
		FrameLayout item_cover = (FrameLayout) view.findViewById(R.id.popview_item);
		TextView popview_item_tv = (TextView) view.findViewById(R.id.popview_item_tv);
		popview_item_tv.setText(context.getString(item.getResource_id()));
		item_cover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listener != null){
					listener.OnViewClicked(item.getCode());
				}
				AiItemMenuDialog.this.dismiss();
			}
		});
		return view;
	}

}
