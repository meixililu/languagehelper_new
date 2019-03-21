package com.messi.languagehelper.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.messi.languagehelper.box.BoxHelper;
import com.messi.languagehelper.box.CNWBean;
import com.messi.languagehelper.util.KeyUtil;
import com.messi.languagehelper.util.LogUtil;
import com.messi.languagehelper.util.Setings;

import java.util.List;

public class MoveDataTask {

    public static void moveCaricatureData(final Context mContext){
        final SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        if(!sp.getBoolean(KeyUtil.HasMoveCaricatureData,false)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<CNWBean> dataList = DataBaseUtil.getInstance().getAllAVObjectData();
                    LogUtil.DefalutLog("moveCaricatureData---dataList:"+dataList.size());
                    if(dataList.size() > 0){
                        BoxHelper.updateCNWBean(dataList);
                        DataBaseUtil.getInstance().clearAvobject();
                    }
                    Setings.saveSharedPreferences(sp,KeyUtil.HasMoveCaricatureData,true);
                    LogUtil.DefalutLog("moveCaricatureData finish");
                }
            }).start();

        }
    }
}
