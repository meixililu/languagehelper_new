package com.messi.languagehelper.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SignUtil {

    public static String getMd5Sign(String key, String... strs){
        String tempStr = "";
        String sign = "";
        List<String> list = new ArrayList<>();
        for(String item : strs){
            list.add(item);
        }
        Collections.sort(list);
        for(String item : list){
            tempStr = tempStr + key + item;
        }
        sign = MD5.encode(tempStr);
        LogUtil.DefalutLog("tempStr:"+tempStr + "---sign:"+sign);
        return sign;
    }
}
