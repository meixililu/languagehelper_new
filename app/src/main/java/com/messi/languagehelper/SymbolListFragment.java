package com.messi.languagehelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcSymbolListAdapter;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.impl.FragmentProgressbarListener;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.leancloud.AVFile;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;

public class SymbolListFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
    private Toolbar mToolbar;
    private ProgressBar progressBar;
    private RcSymbolListAdapter mAdapter;
    private List<SymbolListDao> mSymbolListDao;
    private XFYSAD mXFYSAD;
    private int downloadCount;
    private SharedPreferences sharedPreferences;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                downloadCount++;
                LogUtil.DefalutLog("downloadCount:" + downloadCount);
                if (downloadCount == 48) {
                    ToastUtil.diaplayMesShort(getContext(), "离线完成，您可以离线学习音标了！");
                    downloadCount = 0;
                }
            }
        }
    };

    public static SymbolListFragment getInstance() {
        return new SymbolListFragment();
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.symbol_list_activity, container, false);
        setHasOptionsMenu(true);
        initSwipeRefresh(view);
        initViews(view);
        new QueryTask(this).execute();
        return view;
    }

    private void initViews(View view) {
        sharedPreferences = Setings.getSharedPreferences(getContext());
        mXFYSAD = new XFYSAD(getActivity(), ADUtil.SecondaryPage);
        mSymbolListDao = new ArrayList<SymbolListDao>();
        category_lv = (RecyclerView) view.findViewById(R.id.listview);
        mToolbar = (Toolbar) view.findViewById(R.id.my_awesome_toolbar);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBarCircularIndetermininate);
        mToolbar.setTitle(getString(R.string.title_symbol));
        category_lv.setHasFixedSize(true);
        mAdapter = new RcSymbolListAdapter(mSymbolListDao, mXFYSAD);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setHeader(new Object());
        mAdapter.setItems(mSymbolListDao);
        mXFYSAD.setAdapter(mAdapter);
        category_lv.setAdapter(mAdapter);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        new QueryTask(this).execute();
    }

    private SymbolListDao changeDataType(AVObject avobject) {
        SymbolListDao mBean = new SymbolListDao();
        mBean.setSDCode(avobject.getString(AVOUtil.SymbolDetail.SDCode));
        mBean.setSDName(avobject.getString(AVOUtil.SymbolDetail.SDName));
        mBean.setSDDes(avobject.getString(AVOUtil.SymbolDetail.SDDes));
        mBean.setSDInfo(avobject.getString(AVOUtil.SymbolDetail.SDInfo));
        if (!TextUtils.isEmpty(avobject.getString(AVOUtil.SymbolDetail.SDEnVideoImgUrl))) {
            mBean.setBackup2(avobject.getString(AVOUtil.SymbolDetail.SDEnVideoImgUrl));
        }
        if (!TextUtils.isEmpty(avobject.getString(AVOUtil.SymbolDetail.SDEnVideoUrl))) {
            mBean.setBackup1(avobject.getString(AVOUtil.SymbolDetail.SDEnVideoUrl));
        } else {
            AVFile SDEnVideoFile = avobject.getAVFile(AVOUtil.SymbolDetail.SDEnVideoFile);
            if (SDEnVideoFile != null) {
                mBean.setBackup1(SDEnVideoFile.getUrl());
            }
        }

        if (!TextUtils.isEmpty(avobject.getString(AVOUtil.SymbolDetail.SDAudioMp3Url))) {
            mBean.setSDAudioMp3Url(avobject.getString(AVOUtil.SymbolDetail.SDAudioMp3Url));
        } else {
            AVFile SDAudioMp3File = avobject.getAVFile(AVOUtil.SymbolDetail.SDAudioMp3);
            if (SDAudioMp3File != null) {
                mBean.setSDAudioMp3Url(SDAudioMp3File.getUrl());
            }
        }
        if (!TextUtils.isEmpty(avobject.getString(AVOUtil.SymbolDetail.SDTeacherMp3Url))) {
            mBean.setSDTeacherMp3Url(avobject.getString(AVOUtil.SymbolDetail.SDTeacherMp3Url));
        } else {
            AVFile SDTeacherMp3File = avobject.getAVFile(AVOUtil.SymbolDetail.SDTeacherMp3);
            if (SDTeacherMp3File != null) {
                mBean.setSDTeacherMp3Url(SDTeacherMp3File.getUrl());
            }
        }
        return mBean;
    }

    private class QueryTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<SymbolListFragment> mainActivity;

        public QueryTask(SymbolListFragment mActivity){
            mainActivity = new WeakReference<>(mActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
        }

        @Override
        protected Void doInBackground(Void... params) {
            long localSize = DataBaseUtil.getInstance(getContext()).getSymbolListSize();
            if (sharedPreferences.getString(KeyUtil.UpdateSymbolList, "UpdateSymbolList1").equals("UpdateSymbolList1")) {
                localSize = 0;
                DataBaseUtil.getInstance(getContext()).clearSymbolList();
                Setings.saveSharedPreferences(sharedPreferences, KeyUtil.UpdateSymbolList, "");
            }
            mSymbolListDao.clear();
            if (localSize == 0) {
                AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.SymbolDetail.SymbolDetail);
                query.whereEqualTo(AVOUtil.SymbolDetail.SDIsValid, "1");
                query.orderByAscending(AVOUtil.SymbolDetail.SDCode);
                List<AVObject> avObjects = query.find();
                if (avObjects != null) {
                    for (AVObject mAVObject : avObjects) {
                        mSymbolListDao.add(changeDataType(mAVObject));
                    }
                    DataBaseUtil.getInstance(getContext()).insert(mSymbolListDao);
                }
            } else {
                mSymbolListDao.addAll(DataBaseUtil.getInstance(getContext()).getSymbolList());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(mainActivity.get() != null){
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.symbol_detail_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                showXunFeiDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showXunFeiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setTitle("温馨提示");
        builder.setMessage("下载推荐页48个音标MP3，可离线学习。");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoadUtil.downloadSymbolMp3(getContext(), mSymbolListDao, mHandler);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mXFYSAD != null){
            mXFYSAD.onDestroy();
        }
    }
}
