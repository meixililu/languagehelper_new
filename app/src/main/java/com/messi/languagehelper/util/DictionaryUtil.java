package com.messi.languagehelper.util;

import com.messi.languagehelper.box.Dictionary;

public class DictionaryUtil {
	
	public static String getShareContent(Dictionary mBean) throws Exception{
		if(mBean.getType().equals(KeyUtil.ResultTypeShowapi)){
			return mBean.getWord_name() + "\n" + mBean.getResult();
		}else{
			return mBean.getResult();
		}
	}

}
