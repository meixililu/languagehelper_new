package com.messi.languagehelper.adapter;

import java.util.List;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.wxapi.WXEntryActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WordStudyThirdActivity;
import com.messi.languagehelper.bean.WordListType;
import com.messi.languagehelper.util.KeyUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WordStudySecondAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<WordListType> avObjects;
	
	public WordStudySecondAdapter(Context mContext, List<WordListType> avObjects) {
		context = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		this.avObjects = avObjects;
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
			convertView = mInflater.inflate(R.layout.word_study_root_list_item, null);
			holder = new ViewHolder();
			holder.cover = (View) convertView.findViewById(R.id.layout_cover);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.des = (TextView) convertView.findViewById(R.id.des);
			holder.list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final WordListType mAVObject = avObjects.get(position);
		holder.name.setText( mAVObject.getTitle() );
		holder.des.setText( mAVObject.getCourse_num() + "课    " + mAVObject.getWord_num() + "词");
		
		if(TextUtils.isEmpty(mAVObject.getImg_url())){
			holder.list_item_img.setVisibility(View.GONE);;
		}else{
			holder.list_item_img.setVisibility(View.VISIBLE);
			holder.list_item_img.setImageURI(mAVObject.getImg_url());
		}
		
		holder.cover.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onItemClick(mAVObject);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		View cover;
		TextView name;
		TextView des;
		SimpleDraweeView list_item_img;
	}

	private void onItemClick(WordListType mAVObject){
		WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, mAVObject.getItemList());
		Intent intent = new Intent(context,WordStudyThirdActivity.class);
		intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
		context.startActivity(intent);
	}
	

}
