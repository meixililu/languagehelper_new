package com.messi.languagehelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.R;
import com.messi.languagehelper.WebViewActivity;
import com.messi.languagehelper.dao.BDJContent;
import com.messi.languagehelper.util.KeyUtil;

import java.net.URI;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JokeListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<BDJContent> avObjects;

    public JokeListAdapter(Context mContext, List<BDJContent> avObjects) {
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
            convertView = mInflater.inflate(R.layout.joke_bdj_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final BDJContent mAVObject = avObjects.get(position);
        holder.name.setText(mAVObject.getName());
        holder.des.setText(mAVObject.getText().trim());
        holder.time.setText(mAVObject.getCreate_time());

        holder.profileImage.setImageURI(Uri.parse(mAVObject.getProfile_image()));

        if (mAVObject.getType().equals("10")) {
            holder.listItemImg.setVisibility(View.VISIBLE);
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    .setAutoPlayAnimations(true)
                    .setUri(Uri.parse(mAVObject.getImage2()))
                    .build();
            holder.listItemImg.setController(mDraweeController);
        } else {
            holder.listItemImg.setVisibility(View.GONE);
        }

        if (mAVObject.getType().equals("41") || mAVObject.getType().equals("31")) {
            holder.videoplayer.setVisibility(View.VISIBLE);
            holder.videoplayer.setUp(mAVObject.getVideo_uri(),JCVideoPlayerStandard.SCREEN_LAYOUT_LIST,"");
        }else{
            holder.videoplayer.setVisibility(View.GONE);
        }

        holder.listItemImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(mAVObject);
            }
        });

        return convertView;
    }

    private void onItemClick(BDJContent mAVObject) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getName());
        intent.putExtra(KeyUtil.URL, mAVObject.getImage3());
        context.startActivity(intent);
    }

    class ViewHolder {
        @BindView(R.id.profile_image)
        SimpleDraweeView profileImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.des)
        TextView des;
        @BindView(R.id.list_item_img)
        SimpleDraweeView listItemImg;
        @BindView(R.id.layout_cover)
        LinearLayout layoutCover;
        @BindView(R.id.videoplayer)
        JCVideoPlayerStandard videoplayer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
