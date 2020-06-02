package com.messi.languagehelper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.ScreenUtil;
import com.messi.languagehelper.util.StringUtils;
import com.messi.languagehelper.util.ViewUtil;
import com.ximalaya.ting.android.opensdk.constants.DTransferConstants;
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest;
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack;
import com.ximalaya.ting.android.opensdk.model.album.Album;
import com.ximalaya.ting.android.opensdk.model.album.AlbumList;
import com.ximalaya.ting.android.opensdk.model.album.DiscoveryRecommendAlbums;
import com.ximalaya.ting.android.opensdk.model.album.DiscoveryRecommendAlbumsList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class XmlyCategoryRecommendFragment extends BaseFragment {

    @BindView(R.id.content_tv)
    LinearLayout contentTv;
    Unbinder unbinder;
    private String category;
    private String tag_name;
    private LayoutInflater inflater;
    private int skip = 2;

    public static Fragment newInstance(String category, String tag_name,
                                       FragmentProgressbarListener listener) {
        XmlyCategoryRecommendFragment fragment = new XmlyCategoryRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        bundle.putString("tag_name", tag_name);
        fragment.setArguments(bundle);
        fragment.setListener(listener);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        this.category = mBundle.getString("category");
        this.tag_name = mBundle.getString("tag_name");
        LogUtil.DefalutLog("onCreate:" + category);
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        QueryTask();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.xmly_category_recommend_fragment, container, false);
        initSwipeRefresh(view);
        unbinder = ButterKnife.bind(this, view);
        this.inflater = inflater;
        return view;
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        QueryTask();
    }

    private void QueryTask() {
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.DISPLAY_COUNT, "4");
        CommonRequest.getDiscoveryRecommendAlbums(map, new IDataCallBack<DiscoveryRecommendAlbumsList>() {
            @Override
            public void onSuccess(@Nullable DiscoveryRecommendAlbumsList discoveryRecommendAlbumsList) {
                onFinishLoadData();
                contentTv.removeAllViews();
                final List<DiscoveryRecommendAlbums> list = discoveryRecommendAlbumsList.getDiscoveryRecommendAlbumses();
                if(list != null && !list.isEmpty()){
                    for (int i=0;i<list.size();i++){
                        if(i < 2){
                            setList(list.get(i));
                        }else {
                            final int index = i;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setList(list.get(index));
                                }
                            },700);
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                onFinishLoadData();
            }
        });
    }

    private void onFinishLoadData() {
        hideProgressbar();
        onSwipeRefreshLayoutFinish();
    }

    private void setList(final DiscoveryRecommendAlbums dra){
        contentTv.addView(initView(dra));
    }

    private View initView(final DiscoveryRecommendAlbums dra){
        View view = inflater.inflate(R.layout.xmly_recommend_root,null);
        FrameLayout.LayoutParams mParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParams.bottomMargin = ScreenUtil.dip2px(getContext(),12);
        view.setLayoutParams(mParams);
        FrameLayout header = (FrameLayout) view.findViewById(R.id.xmly_recommend_header);
        TextView header_title = (TextView) view.findViewById(R.id.xmly_recommend_header_title);
        final LinearLayout content_tv = (LinearLayout) view.findViewById(R.id.content_tv);
        FrameLayout footer = (FrameLayout) view.findViewById(R.id.xmly_recommend_footer);
        header_title.setText(dra.getDisplayCategoryName());
        content_tv.removeAllViews();
        for (Album album : dra.getAlbumList()){
            content_tv.addView( initAlbum(album) );
            content_tv.addView(ViewUtil.getLine(getContext()));
        }
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toXmlyCategoryActivity(dra);
            }
        });
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCategoryRecommendList(dra,content_tv);
            }
        });
        return view;
    }

    private View initAlbum(final Album mAVObject){
        View view = inflater.inflate(R.layout.ximalaya_list_item,null);
        FrameLayout layout_cover = (FrameLayout)view.findViewById(R.id.layout_cover);
        SimpleDraweeView list_item_img = (SimpleDraweeView)view.findViewById(R.id.list_item_img);
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView sub_title = (TextView)view.findViewById(R.id.sub_title);
        TextView source_name = (TextView)view.findViewById(R.id.source_name);
        TextView type_name = (TextView)view.findViewById(R.id.type_name);
        title.setText(mAVObject.getAlbumTitle());
        sub_title.setText(mAVObject.getAlbumIntro());
        source_name.setText(StringUtils.numToStrTimes(mAVObject.getPlayCount()));
        type_name.setText(" " + String.valueOf(mAVObject.getIncludeTrackCount()) + " 集");
        list_item_img.setImageURI(Uri.parse(mAVObject.getCoverUrlLarge()));
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_item_playtimes_count);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        source_name.setCompoundDrawables(drawable, null, null, null);
        Drawable dra = this.getResources().getDrawable(R.drawable.ic_item_sounds_count);
        dra.setBounds(0, 0, dra.getMinimumWidth(), dra.getMinimumHeight());
        type_name.setCompoundDrawables(dra, null, null, null);
        layout_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAlbumActivity(mAVObject);
            }
        });
        return view;
    }

    private void getCategoryRecommendList(final DiscoveryRecommendAlbums dra,final LinearLayout content_tv){
        showProgressbar();
        Map<String, String> map = new HashMap<String, String>();
        map.put(DTransferConstants.CATEGORY_ID, dra.getCategoryId());
        map.put(DTransferConstants.CALC_DIMENSION, "1");
        map.put(DTransferConstants.PAGE_SIZE, "4");
        map.put(DTransferConstants.PAGE, String.valueOf(skip));
        CommonRequest.getAlbumList(map, new IDataCallBack<AlbumList>() {
            @Override
            public void onSuccess(@Nullable AlbumList albumList) {
                onFinishLoadData();
                if (albumList != null && albumList.getAlbums() != null) {
                    skip += 1;
                    if (skip > albumList.getTotalPage()) {
                        skip = 1;
                    }
                    content_tv.removeAllViews();
                    for (Album album : albumList.getAlbums()){
                        content_tv.addView( initAlbum(album) );
                        content_tv.addView(ViewUtil.getLine(getContext()));
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                onFinishLoadData();
                LogUtil.DefalutLog(s);
            }
        });
    }

    private void toAlbumActivity(final Album mAVObject) {
        Intent intent = new Intent(getActivity(), XimalayaTrackListActivity.class);
        intent.putExtra("album_id", mAVObject.getId()+"");
        intent.putExtra("play_times", mAVObject.getPlayCount());
        intent.putExtra("track_count", mAVObject.getIncludeTrackCount());
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getAlbumTitle());
        startActivity(intent);
    }

    private void toXmlyCategoryActivity(DiscoveryRecommendAlbums dra){
        Intent intent = new Intent(getContext(),XimalayaTagsActiviry.class);
        intent.putExtra(KeyUtil.Category, dra.getCategoryId());
        intent.putExtra(KeyUtil.ActionbarTitle, dra.getCategoryName());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setListener(FragmentProgressbarListener listener){
        mProgressbarListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
