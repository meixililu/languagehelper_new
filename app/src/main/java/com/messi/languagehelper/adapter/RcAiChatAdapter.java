package com.messi.languagehelper.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.languagehelper.AiChatActivity;
import com.messi.languagehelper.R;
import com.messi.languagehelper.box.AiEntity;

import java.util.List;

public class RcAiChatAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AiEntity, Object> {

	private List<AiEntity> beans;
	private AiChatActivity mAiChatActivity;

	public RcAiChatAdapter(AiChatActivity mAiChatActivity,List<AiEntity> beans){
		this.mAiChatActivity = mAiChatActivity;
		this.beans = beans;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.ai_chat_list_item, parent, false);
		return new RcAiChatItemViewHolder(characterView,beans,this,mAiChatActivity);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		AiEntity mAVObject = getItem(position);
		RcAiChatItemViewHolder mItemViewHolder = (RcAiChatItemViewHolder)holder;
		mItemViewHolder.render(mAVObject);
	}

	private LayoutInflater getLayoutInflater(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext());
	}

	public void addEntity(int position, AiEntity entity) {
		beans.add(position, entity);
		notifyItemInserted(position);
	}
}
