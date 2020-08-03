
package com.messi.languagehelper.box;

import com.messi.languagehelper.box.UserProfileCursor.Factory;
import io.objectbox.EntityInfo;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;
import io.objectbox.internal.IdGetter;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * Properties for entity "UserProfile". Can be used for QueryBuilder and for referencing DB names.
 */
public final class UserProfile_ implements EntityInfo<UserProfile> {

    // Leading underscores for static constants to avoid naming conflicts with property names

    public static final String __ENTITY_NAME = "UserProfile";

    public static final int __ENTITY_ID = 16;

    public static final Class<UserProfile> __ENTITY_CLASS = UserProfile.class;

    public static final String __DB_NAME = "UserProfile";

    public static final CursorFactory<UserProfile> __CURSOR_FACTORY = new Factory();

    @Internal
    static final UserProfileIdGetter __ID_GETTER = new UserProfileIdGetter();

    public final static UserProfile_ __INSTANCE = new UserProfile_();

    public final static io.objectbox.Property<UserProfile> id =
        new io.objectbox.Property<>(__INSTANCE, 0, 1, long.class, "id", true, "id");

    public final static io.objectbox.Property<UserProfile> user_name =
        new io.objectbox.Property<>(__INSTANCE, 1, 2, String.class, "user_name");

    public final static io.objectbox.Property<UserProfile> user_img =
        new io.objectbox.Property<>(__INSTANCE, 2, 3, String.class, "user_img");

    public final static io.objectbox.Property<UserProfile> credits =
        new io.objectbox.Property<>(__INSTANCE, 3, 15, int.class, "credits");

    public final static io.objectbox.Property<UserProfile> continuous =
        new io.objectbox.Property<>(__INSTANCE, 4, 18, int.class, "continuous");

    public final static io.objectbox.Property<UserProfile> show_check_in =
        new io.objectbox.Property<>(__INSTANCE, 5, 16, boolean.class, "show_check_in");

    public final static io.objectbox.Property<UserProfile> check_in_sum =
        new io.objectbox.Property<>(__INSTANCE, 6, 11, int.class, "check_in_sum");

    public final static io.objectbox.Property<UserProfile> last_check_in =
        new io.objectbox.Property<>(__INSTANCE, 7, 12, String.class, "last_check_in");

    public final static io.objectbox.Property<UserProfile> course_score =
        new io.objectbox.Property<>(__INSTANCE, 8, 4, int.class, "course_score");

    public final static io.objectbox.Property<UserProfile> show_level_up =
        new io.objectbox.Property<>(__INSTANCE, 9, 17, boolean.class, "show_level_up");

    public final static io.objectbox.Property<UserProfile> course_unit_sum =
        new io.objectbox.Property<>(__INSTANCE, 10, 13, int.class, "course_unit_sum");

    public final static io.objectbox.Property<UserProfile> course_level_sum =
        new io.objectbox.Property<>(__INSTANCE, 11, 14, int.class, "course_level_sum");

    public final static io.objectbox.Property<UserProfile> backkup =
        new io.objectbox.Property<>(__INSTANCE, 12, 5, String.class, "backkup");

    public final static io.objectbox.Property<UserProfile> backkup1 =
        new io.objectbox.Property<>(__INSTANCE, 13, 6, String.class, "backkup1");

    public final static io.objectbox.Property<UserProfile> backkup2 =
        new io.objectbox.Property<>(__INSTANCE, 14, 7, String.class, "backkup2");

    public final static io.objectbox.Property<UserProfile> backkup3 =
        new io.objectbox.Property<>(__INSTANCE, 15, 8, String.class, "backkup3");

    public final static io.objectbox.Property<UserProfile> backkup4 =
        new io.objectbox.Property<>(__INSTANCE, 16, 9, String.class, "backkup4");

    public final static io.objectbox.Property<UserProfile> backkup5 =
        new io.objectbox.Property<>(__INSTANCE, 17, 10, String.class, "backkup5");

    @SuppressWarnings("unchecked")
    public final static io.objectbox.Property<UserProfile>[] __ALL_PROPERTIES = new io.objectbox.Property[]{
        id,
        user_name,
        user_img,
        credits,
        continuous,
        show_check_in,
        check_in_sum,
        last_check_in,
        course_score,
        show_level_up,
        course_unit_sum,
        course_level_sum,
        backkup,
        backkup1,
        backkup2,
        backkup3,
        backkup4,
        backkup5
    };

    public final static io.objectbox.Property<UserProfile> __ID_PROPERTY = id;

    @Override
    public String getEntityName() {
        return __ENTITY_NAME;
    }

    @Override
    public int getEntityId() {
        return __ENTITY_ID;
    }

    @Override
    public Class<UserProfile> getEntityClass() {
        return __ENTITY_CLASS;
    }

    @Override
    public String getDbName() {
        return __DB_NAME;
    }

    @Override
    public io.objectbox.Property<UserProfile>[] getAllProperties() {
        return __ALL_PROPERTIES;
    }

    @Override
    public io.objectbox.Property<UserProfile> getIdProperty() {
        return __ID_PROPERTY;
    }

    @Override
    public IdGetter<UserProfile> getIdGetter() {
        return __ID_GETTER;
    }

    @Override
    public CursorFactory<UserProfile> getCursorFactory() {
        return __CURSOR_FACTORY;
    }

    @Internal
    static final class UserProfileIdGetter implements IdGetter<UserProfile> {
        @Override
        public long getId(UserProfile object) {
            return object.getId();
        }
    }

}
