
package com.messi.languagehelper.box;

import com.messi.languagehelper.box.EveryDaySentenceCursor.Factory;
import io.objectbox.EntityInfo;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;
import io.objectbox.internal.IdGetter;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * Properties for entity "EveryDaySentence". Can be used for QueryBuilder and for referencing DB names.
 */
public final class EveryDaySentence_ implements EntityInfo<EveryDaySentence> {

    // Leading underscores for static constants to avoid naming conflicts with property names

    public static final String __ENTITY_NAME = "EveryDaySentence";

    public static final int __ENTITY_ID = 6;

    public static final Class<EveryDaySentence> __ENTITY_CLASS = EveryDaySentence.class;

    public static final String __DB_NAME = "EveryDaySentence";

    public static final CursorFactory<EveryDaySentence> __CURSOR_FACTORY = new Factory();

    @Internal
    static final EveryDaySentenceIdGetter __ID_GETTER = new EveryDaySentenceIdGetter();

    public final static EveryDaySentence_ __INSTANCE = new EveryDaySentence_();

    public final static io.objectbox.Property<EveryDaySentence> id =
        new io.objectbox.Property<>(__INSTANCE, 0, 1, Long.class, "id", true, "id");

    public final static io.objectbox.Property<EveryDaySentence> cid =
        new io.objectbox.Property<>(__INSTANCE, 1, 2, Long.class, "cid");

    public final static io.objectbox.Property<EveryDaySentence> sid =
        new io.objectbox.Property<>(__INSTANCE, 2, 3, String.class, "sid");

    public final static io.objectbox.Property<EveryDaySentence> tts =
        new io.objectbox.Property<>(__INSTANCE, 3, 4, String.class, "tts");

    public final static io.objectbox.Property<EveryDaySentence> tts_local_position =
        new io.objectbox.Property<>(__INSTANCE, 4, 5, String.class, "tts_local_position");

    public final static io.objectbox.Property<EveryDaySentence> content =
        new io.objectbox.Property<>(__INSTANCE, 5, 6, String.class, "content");

    public final static io.objectbox.Property<EveryDaySentence> note =
        new io.objectbox.Property<>(__INSTANCE, 6, 7, String.class, "note");

    public final static io.objectbox.Property<EveryDaySentence> love =
        new io.objectbox.Property<>(__INSTANCE, 7, 8, String.class, "love");

    public final static io.objectbox.Property<EveryDaySentence> translation =
        new io.objectbox.Property<>(__INSTANCE, 8, 9, String.class, "translation");

    public final static io.objectbox.Property<EveryDaySentence> picture =
        new io.objectbox.Property<>(__INSTANCE, 9, 10, String.class, "picture");

    public final static io.objectbox.Property<EveryDaySentence> picture2 =
        new io.objectbox.Property<>(__INSTANCE, 10, 11, String.class, "picture2");

    public final static io.objectbox.Property<EveryDaySentence> caption =
        new io.objectbox.Property<>(__INSTANCE, 11, 12, String.class, "caption");

    public final static io.objectbox.Property<EveryDaySentence> dateline =
        new io.objectbox.Property<>(__INSTANCE, 12, 13, String.class, "dateline");

    public final static io.objectbox.Property<EveryDaySentence> s_pv =
        new io.objectbox.Property<>(__INSTANCE, 13, 14, String.class, "s_pv");

    public final static io.objectbox.Property<EveryDaySentence> sp_pv =
        new io.objectbox.Property<>(__INSTANCE, 14, 15, String.class, "sp_pv");

    public final static io.objectbox.Property<EveryDaySentence> fenxiang_img =
        new io.objectbox.Property<>(__INSTANCE, 15, 16, String.class, "fenxiang_img");

    public final static io.objectbox.Property<EveryDaySentence> fenxiang_img_local_position =
        new io.objectbox.Property<>(__INSTANCE, 16, 17, String.class, "fenxiang_img_local_position");

    public final static io.objectbox.Property<EveryDaySentence> backup1 =
        new io.objectbox.Property<>(__INSTANCE, 17, 18, String.class, "backup1");

    public final static io.objectbox.Property<EveryDaySentence> backup2 =
        new io.objectbox.Property<>(__INSTANCE, 18, 19, String.class, "backup2");

    public final static io.objectbox.Property<EveryDaySentence> backup3 =
        new io.objectbox.Property<>(__INSTANCE, 19, 20, String.class, "backup3");

    @SuppressWarnings("unchecked")
    public final static io.objectbox.Property<EveryDaySentence>[] __ALL_PROPERTIES = new io.objectbox.Property[]{
        id,
        cid,
        sid,
        tts,
        tts_local_position,
        content,
        note,
        love,
        translation,
        picture,
        picture2,
        caption,
        dateline,
        s_pv,
        sp_pv,
        fenxiang_img,
        fenxiang_img_local_position,
        backup1,
        backup2,
        backup3
    };

    public final static io.objectbox.Property<EveryDaySentence> __ID_PROPERTY = id;

    @Override
    public String getEntityName() {
        return __ENTITY_NAME;
    }

    @Override
    public int getEntityId() {
        return __ENTITY_ID;
    }

    @Override
    public Class<EveryDaySentence> getEntityClass() {
        return __ENTITY_CLASS;
    }

    @Override
    public String getDbName() {
        return __DB_NAME;
    }

    @Override
    public io.objectbox.Property<EveryDaySentence>[] getAllProperties() {
        return __ALL_PROPERTIES;
    }

    @Override
    public io.objectbox.Property<EveryDaySentence> getIdProperty() {
        return __ID_PROPERTY;
    }

    @Override
    public IdGetter<EveryDaySentence> getIdGetter() {
        return __ID_GETTER;
    }

    @Override
    public CursorFactory<EveryDaySentence> getCursorFactory() {
        return __CURSOR_FACTORY;
    }

    @Internal
    static final class EveryDaySentenceIdGetter implements IdGetter<EveryDaySentence> {
        @Override
        public long getId(EveryDaySentence object) {
            Long id = object.getId();
            return id != null? id : 0;
        }
    }

}