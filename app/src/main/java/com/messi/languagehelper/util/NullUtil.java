package com.messi.languagehelper.util;

import java.util.List;

public class NullUtil {

    public static boolean isNotEmpty(List list){
        return list != null && !list.isEmpty();
    }
}
