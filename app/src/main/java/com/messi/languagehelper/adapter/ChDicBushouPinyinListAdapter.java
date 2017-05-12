package com.messi.languagehelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.ChDicBushouPinyinDetail;
import com.messi.languagehelper.util.Settings;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChDicBushouPinyinListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<ChDicBushouPinyinDetail> mList;
    private String type;

    public ChDicBushouPinyinListAdapter(Context mContext, List<ChDicBushouPinyinDetail> mList, String type) {
        context = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mList = mList;
        this.type = type;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return mList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.chdic_bspy_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ChDicBushouPinyinDetail mAVObject = mList.get(position);
        holder.textJijie.setText(mAVObject.getJijieResult());
        holder.textXiangjie.setText(mAVObject.getXiangjieResult());
        if (mAVObject.isShowXiangjie()) {
            holder.textXiangjie.setVisibility(View.VISIBLE);
        } else {
            holder.textXiangjie.setVisibility(View.GONE);
        }
        holder.fullscreenCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAVObject.setShowXiangjie(!mAVObject.isShowXiangjie());
                if (mAVObject.isShowXiangjie()) {
                    holder.fullscreenImg.setBackgroundResource(R.drawable.ic_fullscreen_exit_grey600_24dp);
                }else{
                    holder.fullscreenImg.setBackgroundResource(R.drawable.ic_fullscreen_grey600_24dp);
                }
                notifyDataSetChanged();
            }
        });
        holder.shareCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.share(context,mAVObject.getShareAndCopy());
            }
        });
        holder.copyCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.copy(context,mAVObject.getShareAndCopy());
            }
        });
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.text_jijie)
        TextView textJijie;
        @BindView(R.id.text_xiangjie)
        TextView textXiangjie;
        @BindView(R.id.share_cover)
        FrameLayout shareCover;
        @BindView(R.id.copy_cover)
        FrameLayout copyCover;
        @BindView(R.id.fullscreen_img)
        ImageView fullscreenImg;
        @BindView(R.id.fullscreen_cover)
        FrameLayout fullscreenCover;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
