package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.bean.TXNewsItem;
import com.messi.languagehelper.util.KeyUtil;

import java.util.List;

public class NewsAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<TXNewsItem> avObjects;
	private String title;

	public NewsAdapter(Context mContext,String title, List<TXNewsItem> avObjects) {
		context = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		this.avObjects = avObjects;
		this.title = title;
	}

	public int getCount() {
		return avObjects.size();
	}

	public Object getItem(int position) {
		return avObjects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.composition_list_item, null);
			holder = new ViewHolder();
			holder.layout_cover = (FrameLayout) convertView.findViewById(R.id.layout_cover);
			holder.list_item_img_parent = (FrameLayout) convertView.findViewById(R.id.list_item_img_parent);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.type_name = (TextView) convertView.findViewById(R.id.type_name);
			holder.source_name = (TextView) convertView.findViewById(R.id.source_name);
			holder.list_item_img = (ImageView) convertView.findViewById(R.id.list_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final TXNewsItem mAVObject = avObjects.get(position);
		final NativeADDataRef mNativeADDataRef = (NativeADDataRef) mAVObject.getmNativeADDataRef();
		if(mNativeADDataRef == null){
			holder.title.setText( mAVObject.getTitle() );
			holder.source_name.setText( mAVObject.getDescription() );

			if(!TextUtils.isEmpty(mAVObject.getPicUrl())){
				holder.list_item_img_parent.setVisibility(View.VISIBLE);
				holder.list_item_img.setVisibility(View.VISIBLE);
				Glide.with(context)
				.load(mAVObject.getPicUrl())
				.into(holder.list_item_img);
			}else{
				holder.list_item_img_parent.setVisibility(View.GONE);
				holder.list_item_img.setVisibility(View.GONE);
			}
			holder.layout_cover.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toDetailActivity(mAVObject);
				}
			});
		}else{
			holder.title.setText( mNativeADDataRef.getSubTitle() );
			holder.type_name.setText(mNativeADDataRef.getTitle());
			holder.source_name.setText("VoiceAds广告");
			holder.list_item_img_parent.setVisibility(View.VISIBLE);
			holder.list_item_img.setVisibility(View.VISIBLE);
			Glide.with(context)
			.load(mNativeADDataRef.getImage())
			.into(holder.list_item_img);
			holder.layout_cover.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mNativeADDataRef.onClicked(v);
				}
			});
		}
		return convertView;
	}
	
	private void toDetailActivity(TXNewsItem mAVObject){
		Intent intent = new Intent(context, WebViewActivity.class);
		intent.putExtra(KeyUtil.ActionbarTitle, title);
		intent.putExtra(KeyUtil.ToolbarBackgroundColorKey, R.color.news_title_bg);
		intent.putExtra(KeyUtil.URL, mAVObject.getUrl());
		context.startActivity(intent);
	}

	static class ViewHolder {
		FrameLayout layout_cover;
		TextView title;
		TextView type_name;
		TextView source_name;
		FrameLayout list_item_img_parent;
		ImageView list_item_img;
	}

}
