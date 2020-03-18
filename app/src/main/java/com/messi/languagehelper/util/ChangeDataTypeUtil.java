package com.messi.languagehelper.util;

import com.avos.avoscloud.AVObject;
import com.messi.languagehelper.box.WordDetailListItem;

/**
 * Created by luli on 17/12/2016.
 */

public class ChangeDataTypeUtil {

    public static WordDetailListItem changeData(AVObject mAVObject) {
        WordDetailListItem mType = new WordDetailListItem();
        mType.setClass_id(mAVObject.getString(AVOUtil.WordStudyDetail.class_id));
        mType.setClass_title(mAVObject.getString(AVOUtil.WordStudyDetail.class_title));
        mType.setCourse(mAVObject.getInt(AVOUtil.WordStudyDetail.course));
        mType.setDesc(mAVObject.getString(AVOUtil.WordStudyDetail.desc));
        mType.setItem_id(mAVObject.getString(AVOUtil.WordStudyDetail.item_id));
        mType.setName(mAVObject.getString(AVOUtil.WordStudyDetail.name));
        mType.setSound(mAVObject.getString(AVOUtil.WordStudyDetail.sound));
        mType.setSymbol(mAVObject.getString(AVOUtil.WordStudyDetail.symbol));
        return mType;
    }
}
