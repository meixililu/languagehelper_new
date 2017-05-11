package com.messi.languagehelper.impl;

import com.messi.languagehelper.dao.Dictionary;
import com.messi.languagehelper.dao.ReadingCategory;

/**
 * Created by luli on 10/26/16.
 */

public interface TablayoutOnSelectedListener {

    public void onTabSelectedListener(ReadingCategory mReadingCategory);

    public void onTabReselectedListener(ReadingCategory mReadingCategory);

}
