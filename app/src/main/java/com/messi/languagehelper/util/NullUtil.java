package com.messi.languagehelper.util;

import java.util.List;

public class NullUtil {

    public static boolean isEmpty(List list){
        return list != null && !list.isEmpty();
    }
}
