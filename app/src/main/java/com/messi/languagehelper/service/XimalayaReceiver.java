package com.messi.languagehelper.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.messi.languagehelper.util.LogUtil;

/**
 * Created by luli on 16/11/2017.
 */

public class XimalayaReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.DefalutLog("XimalayaReceiver:"+intent.getAction());
    }
}
