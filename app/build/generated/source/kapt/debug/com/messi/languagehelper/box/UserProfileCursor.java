package com.messi.languagehelper.box;

import io.objectbox.BoxStore;
import io.objectbox.Cursor;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * ObjectBox generated Cursor implementation for "UserProfile".
 * Note that this is a low-level class: usually you should stick to the Box class.
 */
public final class UserProfileCursor extends Cursor<UserProfile> {
    @Internal
    static final class Factory implements CursorFactory<UserProfile> {
        @Override
        public Cursor<UserProfile> createCursor(io.objectbox.Transaction tx, long cursorHandle, BoxStore boxStoreForEntities) {
            return new UserProfileCursor(tx, cursorHandle, boxStoreForEntities);
        }
    }

    private static final UserProfile_.UserProfileIdGetter ID_GETTER = UserProfile_.__ID_GETTER;


    private final static int __ID_user_name = UserProfile_.user_name.id;
    private final static int __ID_user_img = UserProfile_.user_img.id;
    private final static int __ID_credits = UserProfile_.credits.id;
    private final static int __ID_continuous = UserProfile_.continuous.id;
    private final static int __ID_show_check_in = UserProfile_.show_check_in.id;
    private final static int __ID_check_in_sum = UserProfile_.check_in_sum.id;
    private final static int __ID_last_check_in = UserProfile_.last_check_in.id;
    private final static int __ID_course_score = UserProfile_.course_score.id;
    private final static int __ID_show_level_up = UserProfile_.show_level_up.id;
    private final static int __ID_course_unit_sum = UserProfile_.course_unit_sum.id;
    private final static int __ID_course_level_sum = UserProfile_.course_level_sum.id;
    private final static int __ID_backkup = UserProfile_.backkup.id;
    private final static int __ID_backkup1 = UserProfile_.backkup1.id;
    private final static int __ID_backkup2 = UserProfile_.backkup2.id;
    private final static int __ID_backkup3 = UserProfile_.backkup3.id;
    private final static int __ID_backkup4 = UserProfile_.backkup4.id;
    private final static int __ID_backkup5 = UserProfile_.backkup5.id;

    public UserProfileCursor(io.objectbox.Transaction tx, long cursor, BoxStore boxStore) {
        super(tx, cursor, UserProfile_.__INSTANCE, boxStore);
    }

    @Override
    public final long getId(UserProfile entity) {
        return ID_GETTER.getId(entity);
    }

    /**
     * Puts an object into its box.
     *
     * @return The ID of the object within its box.
     */
    @Override
    public final long put(UserProfile entity) {
        String user_name = entity.getUser_name();
        int __id1 = user_name != null ? __ID_user_name : 0;
        String user_img = entity.getUser_img();
        int __id2 = user_img != null ? __ID_user_img : 0;
        String last_check_in = entity.getLast_check_in();
        int __id7 = last_check_in != null ? __ID_last_check_in : 0;
        String backkup = entity.getBackkup();
        int __id12 = backkup != null ? __ID_backkup : 0;

        collect400000(cursor, 0, PUT_FLAG_FIRST,
                __id1, user_name, __id2, user_img,
                __id7, last_check_in, __id12, backkup);

        String backkup1 = entity.getBackkup1();
        int __id13 = backkup1 != null ? __ID_backkup1 : 0;
        String backkup2 = entity.getBackkup2();
        int __id14 = backkup2 != null ? __ID_backkup2 : 0;
        String backkup3 = entity.getBackkup3();
        int __id15 = backkup3 != null ? __ID_backkup3 : 0;
        String backkup4 = entity.getBackkup4();
        int __id16 = backkup4 != null ? __ID_backkup4 : 0;

        collect400000(cursor, 0, 0,
                __id13, backkup1, __id14, backkup2,
                __id15, backkup3, __id16, backkup4);

        String backkup5 = entity.getBackkup5();
        int __id17 = backkup5 != null ? __ID_backkup5 : 0;

        collect313311(cursor, 0, 0,
                __id17, backkup5, 0, null,
                0, null, 0, null,
                __ID_credits, entity.getCredits(), __ID_continuous, entity.getContinuous(),
                __ID_check_in_sum, entity.getCheck_in_sum(), __ID_course_score, entity.getCourse_score(),
                __ID_course_unit_sum, entity.getCourse_unit_sum(), __ID_course_level_sum, entity.getCourse_level_sum(),
                0, 0, 0, 0);

        long __assignedId = collect004000(cursor, entity.getId(), PUT_FLAG_COMPLETE,
                __ID_show_check_in, entity.getShow_check_in() ? 1 : 0, __ID_show_level_up, entity.getShow_level_up() ? 1 : 0,
                0, 0, 0, 0);

        entity.setId(__assignedId);

        return __assignedId;
    }

}
