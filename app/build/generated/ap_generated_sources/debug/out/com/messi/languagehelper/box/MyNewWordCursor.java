package com.messi.languagehelper.box;

import io.objectbox.BoxStore;
import io.objectbox.Cursor;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * ObjectBox generated Cursor implementation for "MyNewWord".
 * Note that this is a low-level class: usually you should stick to the Box class.
 */
public final class MyNewWordCursor extends Cursor<MyNewWord> {
    @Internal
    static final class Factory implements CursorFactory<MyNewWord> {
        @Override
        public Cursor<MyNewWord> createCursor(io.objectbox.Transaction tx, long cursorHandle, BoxStore boxStoreForEntities) {
            return new MyNewWordCursor(tx, cursorHandle, boxStoreForEntities);
        }
    }

    private static final MyNewWord_.MyNewWordIdGetter ID_GETTER = MyNewWord_.__ID_GETTER;


    private final static int __ID_english = MyNewWord_.english.id;
    private final static int __ID_chinese = MyNewWord_.chinese.id;
    private final static int __ID_resultAudioPath = MyNewWord_.resultAudioPath.id;
    private final static int __ID_questionAudioPath = MyNewWord_.questionAudioPath.id;
    private final static int __ID_questionVoiceId = MyNewWord_.questionVoiceId.id;
    private final static int __ID_resultVoiceId = MyNewWord_.resultVoiceId.id;
    private final static int __ID_iscollected = MyNewWord_.iscollected.id;
    private final static int __ID_visit_times = MyNewWord_.visit_times.id;
    private final static int __ID_speak_speed = MyNewWord_.speak_speed.id;
    private final static int __ID_backup1 = MyNewWord_.backup1.id;
    private final static int __ID_backup2 = MyNewWord_.backup2.id;
    private final static int __ID_backup3 = MyNewWord_.backup3.id;

    public MyNewWordCursor(io.objectbox.Transaction tx, long cursor, BoxStore boxStore) {
        super(tx, cursor, MyNewWord_.__INSTANCE, boxStore);
    }

    @Override
    public final long getId(MyNewWord entity) {
        return ID_GETTER.getId(entity);
    }

    /**
     * Puts an object into its box.
     *
     * @return The ID of the object within its box.
     */
    @Override
    public final long put(MyNewWord entity) {
        String english = entity.getEnglish();
        int __id1 = english != null ? __ID_english : 0;
        String chinese = entity.getChinese();
        int __id2 = chinese != null ? __ID_chinese : 0;
        String resultAudioPath = entity.getResultAudioPath();
        int __id3 = resultAudioPath != null ? __ID_resultAudioPath : 0;
        String questionAudioPath = entity.getQuestionAudioPath();
        int __id4 = questionAudioPath != null ? __ID_questionAudioPath : 0;

        collect400000(cursor, 0, PUT_FLAG_FIRST,
                __id1, english, __id2, chinese,
                __id3, resultAudioPath, __id4, questionAudioPath);

        String questionVoiceId = entity.getQuestionVoiceId();
        int __id5 = questionVoiceId != null ? __ID_questionVoiceId : 0;
        String resultVoiceId = entity.getResultVoiceId();
        int __id6 = resultVoiceId != null ? __ID_resultVoiceId : 0;
        String iscollected = entity.getIscollected();
        int __id7 = iscollected != null ? __ID_iscollected : 0;
        String backup1 = entity.getBackup1();
        int __id10 = backup1 != null ? __ID_backup1 : 0;

        collect400000(cursor, 0, 0,
                __id5, questionVoiceId, __id6, resultVoiceId,
                __id7, iscollected, __id10, backup1);

        Long id = entity.getId();
        String backup2 = entity.getBackup2();
        int __id11 = backup2 != null ? __ID_backup2 : 0;
        String backup3 = entity.getBackup3();
        int __id12 = backup3 != null ? __ID_backup3 : 0;
        Integer visit_times = entity.getVisit_times();
        int __id8 = visit_times != null ? __ID_visit_times : 0;
        Integer speak_speed = entity.getSpeak_speed();
        int __id9 = speak_speed != null ? __ID_speak_speed : 0;

        long __assignedId = collect313311(cursor, id != null ? id: 0, PUT_FLAG_COMPLETE,
                __id11, backup2, __id12, backup3,
                0, null, 0, null,
                __id8, __id8 != 0 ? visit_times : 0, __id9, __id9 != 0 ? speak_speed : 0,
                0, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0);

        entity.setId(__assignedId);

        return __assignedId;
    }

}
