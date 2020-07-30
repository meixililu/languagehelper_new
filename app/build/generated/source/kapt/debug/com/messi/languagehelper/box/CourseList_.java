
package com.messi.languagehelper.box;

import com.messi.languagehelper.box.CourseListCursor.Factory;
import io.objectbox.EntityInfo;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;
import io.objectbox.internal.IdGetter;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * Properties for entity "CourseList". Can be used for QueryBuilder and for referencing DB names.
 */
public final class CourseList_ implements EntityInfo<CourseList> {

    // Leading underscores for static constants to avoid naming conflicts with property names

    public static final String __ENTITY_NAME = "CourseList";

    public static final int __ENTITY_ID = 15;

    public static final Class<CourseList> __ENTITY_CLASS = CourseList.class;

    public static final String __DB_NAME = "CourseList";

    public static final CursorFactory<CourseList> __CURSOR_FACTORY = new Factory();

    @Internal
    static final CourseListIdGetter __ID_GETTER = new CourseListIdGetter();

    public final static CourseList_ __INSTANCE = new CourseList_();

    public final static io.objectbox.Property<CourseList> id =
        new io.objectbox.Property<>(__INSTANCE, 0, 1, long.class, "id", true, "id");

    public final static io.objectbox.Property<CourseList> objectId =
        new io.objectbox.Property<>(__INSTANCE, 1, 2, String.class, "objectId");

    public final static io.objectbox.Property<CourseList> course_id =
        new io.objectbox.Property<>(__INSTANCE, 2, 3, String.class, "course_id");

    public final static io.objectbox.Property<CourseList> name =
        new io.objectbox.Property<>(__INSTANCE, 3, 4, String.class, "name");

    public final static io.objectbox.Property<CourseList> course_num =
        new io.objectbox.Property<>(__INSTANCE, 4, 5, int.class, "course_num");

    public final static io.objectbox.Property<CourseList> current =
        new io.objectbox.Property<>(__INSTANCE, 5, 6, int.class, "current");

    public final static io.objectbox.Property<CourseList> order =
        new io.objectbox.Property<>(__INSTANCE, 6, 7, int.class, "order");

    public final static io.objectbox.Property<CourseList> to_activity =
        new io.objectbox.Property<>(__INSTANCE, 7, 13, String.class, "to_activity");

    public final static io.objectbox.Property<CourseList> img =
        new io.objectbox.Property<>(__INSTANCE, 8, 8, String.class, "img");

    public final static io.objectbox.Property<CourseList> type =
        new io.objectbox.Property<>(__INSTANCE, 9, 21, String.class, "type");

    public final static io.objectbox.Property<CourseList> lock =
        new io.objectbox.Property<>(__INSTANCE, 10, 20, String.class, "lock");

    public final static io.objectbox.Property<CourseList> backkup =
        new io.objectbox.Property<>(__INSTANCE, 11, 14, String.class, "backkup");

    public final static io.objectbox.Property<CourseList> backkup1 =
        new io.objectbox.Property<>(__INSTANCE, 12, 15, String.class, "backkup1");

    public final static io.objectbox.Property<CourseList> backkup2 =
        new io.objectbox.Property<>(__INSTANCE, 13, 16, String.class, "backkup2");

    public final static io.objectbox.Property<CourseList> backkup3 =
        new io.objectbox.Property<>(__INSTANCE, 14, 17, String.class, "backkup3");

    public final static io.objectbox.Property<CourseList> backkup4 =
        new io.objectbox.Property<>(__INSTANCE, 15, 18, String.class, "backkup4");

    public final static io.objectbox.Property<CourseList> backkup5 =
        new io.objectbox.Property<>(__INSTANCE, 16, 19, String.class, "backkup5");

    public final static io.objectbox.Property<CourseList> finish =
        new io.objectbox.Property<>(__INSTANCE, 17, 22, boolean.class, "finish");

    public final static io.objectbox.Property<CourseList> user_level_num =
        new io.objectbox.Property<>(__INSTANCE, 18, 23, int.class, "user_level_num");

    public final static io.objectbox.Property<CourseList> user_unit_num =
        new io.objectbox.Property<>(__INSTANCE, 19, 24, int.class, "user_unit_num");

    public final static io.objectbox.Property<CourseList> level_num =
        new io.objectbox.Property<>(__INSTANCE, 20, 10, int.class, "level_num");

    public final static io.objectbox.Property<CourseList> unit_num =
        new io.objectbox.Property<>(__INSTANCE, 21, 11, int.class, "unit_num");

    public final static io.objectbox.Property<CourseList> views =
        new io.objectbox.Property<>(__INSTANCE, 22, 12, int.class, "views");

    @SuppressWarnings("unchecked")
    public final static io.objectbox.Property<CourseList>[] __ALL_PROPERTIES = new io.objectbox.Property[]{
        id,
        objectId,
        course_id,
        name,
        course_num,
        current,
        order,
        to_activity,
        img,
        type,
        lock,
        backkup,
        backkup1,
        backkup2,
        backkup3,
        backkup4,
        backkup5,
        finish,
        user_level_num,
        user_unit_num,
        level_num,
        unit_num,
        views
    };

    public final static io.objectbox.Property<CourseList> __ID_PROPERTY = id;

    @Override
    public String getEntityName() {
        return __ENTITY_NAME;
    }

    @Override
    public int getEntityId() {
        return __ENTITY_ID;
    }

    @Override
    public Class<CourseList> getEntityClass() {
        return __ENTITY_CLASS;
    }

    @Override
    public String getDbName() {
        return __DB_NAME;
    }

    @Override
    public io.objectbox.Property<CourseList>[] getAllProperties() {
        return __ALL_PROPERTIES;
    }

    @Override
    public io.objectbox.Property<CourseList> getIdProperty() {
        return __ID_PROPERTY;
    }

    @Override
    public IdGetter<CourseList> getIdGetter() {
        return __ID_GETTER;
    }

    @Override
    public CursorFactory<CourseList> getCursorFactory() {
        return __CURSOR_FACTORY;
    }

    @Internal
    static final class CourseListIdGetter implements IdGetter<CourseList> {
        @Override
        public long getId(CourseList object) {
            return object.getId();
        }
    }

}
