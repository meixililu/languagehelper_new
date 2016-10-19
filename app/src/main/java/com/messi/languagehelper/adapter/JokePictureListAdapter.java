package com.messi.languagehelper.adapter;

import java.util.List;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.ViewImageActivity;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.dao.BDJContent;
import com.messi.languagehelper.dao.JokeContent;
import com.messi.languagehelper.util.KeyUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class JokePictureListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<JokeContent> avObjects;
	
	public JokePictureListAdapter(Context mContext, List<JokeContent> avObjects) {
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
			convertView = mInflater.inflate(R.layout.joke_picture_list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final JokeContent mAVObject = avObjects.get(position);
		holder.name.setText( mAVObject.getTitle() );
		holder.list_item_img.setImageURI(Uri.parse(mAVObject.getImg()));

		holder.list_item_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onItemClick(mAVObject);
			}
		});
		return convertView;
	}

	private void onItemClick(JokeContent mAVObject) {
		Intent intent = new Intent(context, ViewImageActivity.class);
		intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
		intent.putExtra(KeyUtil.BigImgUrl, mAVObject.getImg());
		context.startActivity(intent);
	}

	static class ViewHolder {
		TextView name;
		SimpleDraweeView list_item_img;
	}

}
