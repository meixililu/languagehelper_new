package com.messi.languagehelper.impl;

import com.messi.languagehelper.bean.ReadingCategory;

/**
 * Created by luli on 10/26/16.
 */

public interface TablayoutOnSelectedListener {

    public void onTabSelectedListener(int pos,ReadingCategory mReadingCategory);

    public void onTabReselectedListener(int pos,ReadingCategory mReadingCategory);

}
