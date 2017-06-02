package com.messi.languagehelper;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.languagehelper.adapter.RcSymbolListAdapter;
import com.messi.languagehelper.dao.SymbolListDao;
import com.messi.languagehelper.db.DataBaseUtil;
import com.messi.languagehelper.util.ADUtil;
import com.messi.languagehelper.util.AVOUtil;
import com.messi.languagehelper.util.DownLoadUtil;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Settings;
import com.messi.languagehelper.util.ToastUtil;
import com.messi.languagehelper.util.XFYSAD;
import com.messi.languagehelper.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SymbolListActivity extends BaseActivity {

    private static final int NUMBER_OF_COLUMNS = 3;
    private RecyclerView category_lv;
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
                    ToastUtil.diaplayMesShort(SymbolListActivity.this, "离线完成，您可以离线学习音标了！");
                    downloadCount = 0;
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.symbol_list_activity);
        initSwipeRefresh();
        initViews();
        new QueryTask().execute();
    }

    private void initViews() {
        getSupportActionBar().setTitle(getResources().getString(R.string.symbolStudy));
        sharedPreferences = Settings.getSharedPreferences(this);
        mXFYSAD = new XFYSAD(SymbolListActivity.this, ADUtil.SecondaryPage);
        mSymbolListDao = new ArrayList<SymbolListDao>();
        category_lv = (RecyclerView) findViewById(R.id.listview);
        category_lv.setHasFixedSize(true);
        mAdapter = new RcSymbolListAdapter(mSymbolListDao, mXFYSAD);
        GridLayoutManager layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        mAdapter.setHeader(new Object());
        mAdapter.setItems(mSymbolListDao);
        category_lv.setAdapter(mAdapter);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        new QueryTask().execute();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.symbol_detail_menu, menu);
        return true;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("下载所有音标MP3，可离线学习。");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoadUtil.downloadSymbolMp3(SymbolListActivity.this, mSymbolListDao, mHandler);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class QueryTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressbar();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                long localSize = DataBaseUtil.getInstance().getSymbolListSize();
                if (sharedPreferences.getString(KeyUtil.UpdateSymbolList, "UpdateSymbolList1").equals("UpdateSymbolList1")) {
                    localSize = 0;
                    DataBaseUtil.getInstance().clearSymbolList();
                    Settings.saveSharedPreferences(sharedPreferences, KeyUtil.UpdateSymbolList, "");
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
                        DataBaseUtil.getInstance().insert(mSymbolListDao);
                    }
                } else {
                    mSymbolListDao.addAll(DataBaseUtil.getInstance().getSymbolList());
                }

            } catch (AVException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            mAdapter.notifyDataSetChanged();
        }
    }
}
