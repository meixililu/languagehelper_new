package com.messi.languagehelper.util;

import java.util.List;

import android.text.TextUtils;

import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.Means;
import com.messi.languagehelper.dao.Parts;

public class DictionaryUtil {
	
	public static String getShareContent(Dictionary mBean) throws Exception{
		if(mBean.getType().equals(KeyUtil.ResultTypeShowapi)){
			return mBean.getWord_name() + "\n" + mBean.getResult();
		}else{
			return mBean.getResult();
		}
	}

}
