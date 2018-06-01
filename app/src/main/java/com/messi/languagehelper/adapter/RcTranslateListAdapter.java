package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVObject;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.ImgShareActivity;
import com.messi.languagehelper.MainFragment;
import com.messi.languagehelper.PracticeActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.dao.record;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.dialog.PopDialog;
import com.messi.languagehelper.dialog.PopDialog.PopViewItemOnclickListener;
import com.messi.languagehelper.task.MyThread;
import com.messi.languagehelper.util.AudioTrackUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.SDCardUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.ViewUtil;
import com.messi.languagehelper.util.XFUtil;
import com.messi.languagehelper.util.XFYSAD;

import java.util.List;

public class RcTranslateListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, record, Object> {

	private List<record> beans;

	public RcTranslateListAdapter(List<record> beans){
		this.beans = beans;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_recent_used, parent, false);
		return new RcTranslateLiatItemViewHolder(characterView,beans,this);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		record mAVObject = getItem(position);
		RcTranslateLiatItemViewHolder mItemViewHolder = (RcTranslateLiatItemViewHolder)holder;
		mItemViewHolder.render(mAVObject);
	}

	@Override
	protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
		return new RcLmFooterViewHolder(ViewUtil.getListFooterView(parent.getContext()));
	}

	@Override
	protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
		super.onBindFooterViewHolder(holder, position);
	}

	private LayoutInflater getLayoutInflater(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext());
	}

	public void addEntity(int position, record entity) {
		beans.add(position, entity);
		notifyItemInserted(position);
	}
}
