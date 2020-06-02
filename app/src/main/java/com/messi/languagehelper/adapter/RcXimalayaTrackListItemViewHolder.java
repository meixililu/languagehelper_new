package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.XimalayaDetailActivity;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.TimeUtil;
import com.ximalaya.ting.android.opensdk.model.track.Track;
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcXimalayaTrackListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final TextView publish_time;
    private final ImageView music_play_img;
    private final SimpleDraweeView list_item_img;
    private RecyclerView.Adapter mAdapter;

    private List<Track> trackList;
    private Context context;

    public RcXimalayaTrackListItemViewHolder(View itemView,List<Track> trackList,RecyclerView.Adapter mAdapter) {
        super(itemView);
        this.context = itemView.getContext();
        this.trackList = trackList;
        this.mAdapter = mAdapter;
        layout_cover = (FrameLayout) itemView.findViewById(R.id.layout_cover);
        title = (TextView) itemView.findViewById(R.id.title);
        type_name = (TextView) itemView.findViewById(R.id.type_name);
        source_name = (TextView) itemView.findViewById(R.id.source_name);
        publish_time = (TextView) itemView.findViewById(R.id.publish_time);
        music_play_img = (ImageView) itemView.findViewById(R.id.music_play_img);
        list_item_img = (SimpleDraweeView) itemView.findViewById(R.id.list_item_img);

    }

    public void render(final Track mAVObject) {
        title.setText(mAVObject.getTrackTitle());
        source_name.setText(StringUtils.numToStrTimes(mAVObject.getPlayCount()));
        type_name.setText( TimeUtil.getDuration(mAVObject.getDuration()) );
        publish_time.setText(TimeUtil.getTimeBefore(mAVObject.getCreatedAt()));
        list_item_img.setImageURI(Uri.parse(mAVObject.getCoverUrlLarge()));
        music_play_img.setImageResource(R.drawable.ic_play_white);
        if(mAVObject.isUpdateStatus()){
            music_play_img.setImageResource(R.drawable.ic_pause_white);
        }
        if(XmPlayerManager.getInstance(context).isPlaying()){
            if(XmPlayerManager.getInstance(context).getCurrSound() instanceof Track){
                Track mTrack = (Track)XmPlayerManager.getInstance(context).getCurrSound();
                if(mTrack.getDataId() == mAVObject.getDataId()){
                    music_play_img.setImageResource(R.drawable.ic_pause_white);
                    mAVObject.setUpdateStatus(true);
                }
            }
        }
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDetailActivity(getAdapterPosition());
            }
        });
    }

    private void toDetailActivity(int position){
        LogUtil.DefalutLog(trackList.get(position).toString());
        Intent intent = new Intent(context, XimalayaDetailActivity.class);
        intent.putExtra(KeyUtil.PositionKey,position);
        Setings.dataMap.put(KeyUtil.List,trackList);
        context.startActivity(intent);
    }

}
