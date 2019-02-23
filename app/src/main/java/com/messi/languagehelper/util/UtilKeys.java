package com.messi.languagehelper.util;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.List;

public class UtilKeys {

    public static void getTranBDKey(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.UtilKeys.UtilKeys);
                    query.whereEqualTo(AVOUtil.UtilKeys.name, "bd_en_tran");
                    List<AVObject> list = query.find();
                    if(list != null && list.size() > 0){
                        AVObject mAVObject = list.get(0);
                        Setings.baidu_appid = mAVObject.getString(AVOUtil.UtilKeys.app_id);
                        Setings.baidu_secretkey = mAVObject.getString(AVOUtil.UtilKeys.kvalue);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
}


