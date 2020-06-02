package com.messi.languagehelper.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.leancloud.AVObject;
import com.messi.languagehelper.R;
import com.messi.languagehelper.StudyDialogListActivity;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.KeyUtil;

public class StudyDialogCategoryAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<AVObject> avObjects;

	public StudyDialogCategoryAdapter(Context mContext, List<AVObject> avObjects) {
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
			convertView = mInflater.inflate(R.layout.studylist_item, null);
			holder = new ViewHolder();
			holder.cover = (View) convertView.findViewById(R.id.layout_cover);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final AVObject mAVObject = avObjects.get(position);
		holder.name.setText( mAVObject.getString(AVOUtil.StudyDialogCategory.SDName) );
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
	}

	private void onItemClick(AVObject mAVObject){
		Intent intent = new Intent(context,StudyDialogListActivity.class);
		intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getString(AVOUtil.StudyDialogCategory.SDName));
		intent.putExtra(AVOUtil.StudyDialogCategory.SDCode, mAVObject.getString(AVOUtil.StudyDialogCategory.SDCode));
		context.startActivity(intent);
	}
	

}
