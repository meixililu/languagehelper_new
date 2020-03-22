package com.messi.languagehelper.util;

import android.app.Application;
import android.content.Context;

import com.messi.languagehelper.provider.ContextProvider;

public class ContextUtil {

    private static ContextUtil instance;

    private Context mContext;

    private ContextUtil(Context mContext){
        this.mContext = mContext;
    }

    public static ContextUtil get(){
        if (instance == null) {
            synchronized (ContextUtil.class) {
                if (instance == null) {
                    Context context = ContextProvider.mContext;
                    if (context == null) {
                        throw new IllegalStateException("context is null");
                    }
                    instance = new ContextUtil(context);
                }
            }
        }
        return instance;
    }

    public Context getContext(){
        return mContext;
    }

    public Application getApplication(){
        return (Application) mContext.getApplicationContext();
    }
}
