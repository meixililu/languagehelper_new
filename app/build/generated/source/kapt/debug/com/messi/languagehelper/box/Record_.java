
package com.messi.languagehelper.box;

import com.messi.languagehelper.box.RecordCursor.Factory;
import io.objectbox.EntityInfo;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;
import io.objectbox.internal.IdGetter;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * Properties for entity "Record". Can be used for QueryBuilder and for referencing DB names.
 */
public final class Record_ implements EntityInfo<Record> {

    // Leading underscores for static constants to avoid naming conflicts with property names

    public static final String __ENTITY_NAME = "Record";

    public static final int __ENTITY_ID = 7;

    public static final Class<Record> __ENTITY_CLASS = Record.class;

    public static final String __DB_NAME = "Record";

    public static final CursorFactory<Record> __CURSOR_FACTORY = new Factory();

    @Internal
    static final RecordIdGetter __ID_GETTER = new RecordIdGetter();

    public final static Record_ __INSTANCE = new Record_();

    public final static io.objectbox.Property<Record> id =
        new io.objectbox.Property<>(__INSTANCE, 0, 1, Long.class, "id", true, "id");

    public final static io.objectbox.Property<Record> english =
        new io.objectbox.Property<>(__INSTANCE, 1, 2, String.class, "english");

    public final static io.objectbox.Property<Record> chinese =
        new io.objectbox.Property<>(__INSTANCE, 2, 3, String.class, "chinese");

    public final static io.objectbox.Property<Record> resultAudioPath =
        new io.objectbox.Property<>(__INSTANCE, 3, 4, String.class, "resultAudioPath");

    public final static io.objectbox.Property<Record> questionAudioPath =
        new io.objectbox.Property<>(__INSTANCE, 4, 5, String.class, "questionAudioPath");

    public final static io.objectbox.Property<Record> questionVoiceId =
        new io.objectbox.Property<>(__INSTANCE, 5, 6, String.class, "questionVoiceId");

    public final static io.objectbox.Property<Record> resultVoiceId =
        new io.objectbox.Property<>(__INSTANCE, 6, 7, String.class, "resultVoiceId");

    public final static io.objectbox.Property<Record> iscollected =
        new io.objectbox.Property<>(__INSTANCE, 7, 8, String.class, "iscollected");

    public final static io.objectbox.Property<Record> ph_am_mp3 =
        new io.objectbox.Property<>(__INSTANCE, 8, 14, String.class, "ph_am_mp3");

    public final static io.objectbox.Property<Record> ph_en_mp3 =
        new io.objectbox.Property<>(__INSTANCE, 9, 15, String.class, "ph_en_mp3");

    public final static io.objectbox.Property<Record> ph_tts_mp3 =
        new io.objectbox.Property<>(__INSTANCE, 10, 16, String.class, "ph_tts_mp3");

    public final static io.objectbox.Property<Record> des =
        new io.objectbox.Property<>(__INSTANCE, 11, 17, String.class, "des");

    public final static io.objectbox.Property<Record> examples =
        new io.objectbox.Property<>(__INSTANCE, 12, 18, String.class, "examples");

    public final static io.objectbox.Property<Record> paraphrase =
        new io.objectbox.Property<>(__INSTANCE, 13, 19, String.class, "paraphrase");

    public final static io.objectbox.Property<Record> en_paraphrase =
        new io.objectbox.Property<>(__INSTANCE, 14, 20, String.class, "en_paraphrase");

    public final static io.objectbox.Property<Record> au_paraphrase =
        new io.objectbox.Property<>(__INSTANCE, 15, 21, String.class, "au_paraphrase");

    public final static io.objectbox.Property<Record> dicts =
        new io.objectbox.Property<>(__INSTANCE, 16, 22, String.class, "dicts");

    public final static io.objectbox.Property<Record> examinations =
        new io.objectbox.Property<>(__INSTANCE, 17, 23, String.class, "examinations");

    public final static io.objectbox.Property<Record> root =
        new io.objectbox.Property<>(__INSTANCE, 18, 24, String.class, "root");

    public final static io.objectbox.Property<Record> tense =
        new io.objectbox.Property<>(__INSTANCE, 19, 25, String.class, "tense");

    public final static io.objectbox.Property<Record> type =
        new io.objectbox.Property<>(__INSTANCE, 20, 26, String.class, "type");

    public final static io.objectbox.Property<Record> visit_times =
        new io.objectbox.Property<>(__INSTANCE, 21, 9, Integer.class, "visit_times");

    public final static io.objectbox.Property<Record> speak_speed =
        new io.objectbox.Property<>(__INSTANCE, 22, 10, Integer.class, "speak_speed");

    public final static io.objectbox.Property<Record> backup1 =
        new io.objectbox.Property<>(__INSTANCE, 23, 11, String.class, "backup1");

    public final static io.objectbox.Property<Record> backup2 =
        new io.objectbox.Property<>(__INSTANCE, 24, 12, String.class, "backup2");

    public final static io.objectbox.Property<Record> backup3 =
        new io.objectbox.Property<>(__INSTANCE, 25, 13, String.class, "backup3");

    @SuppressWarnings("unchecked")
    public final static io.objectbox.Property<Record>[] __ALL_PROPERTIES = new io.objectbox.Property[]{
        id,
        english,
        chinese,
        resultAudioPath,
        questionAudioPath,
        questionVoiceId,
        resultVoiceId,
        iscollected,
        ph_am_mp3,
        ph_en_mp3,
        ph_tts_mp3,
        des,
        examples,
        paraphrase,
        en_paraphrase,
        au_paraphrase,
        dicts,
        examinations,
        root,
        tense,
        type,
        visit_times,
        speak_speed,
        backup1,
        backup2,
        backup3
    };

    public final static io.objectbox.Property<Record> __ID_PROPERTY = id;

    @Override
    public String getEntityName() {
        return __ENTITY_NAME;
    }

    @Override
    public int getEntityId() {
        return __ENTITY_ID;
    }

    @Override
    public Class<Record> getEntityClass() {
        return __ENTITY_CLASS;
    }

    @Override
    public String getDbName() {
        return __DB_NAME;
    }

    @Override
    public io.objectbox.Property<Record>[] getAllProperties() {
        return __ALL_PROPERTIES;
    }

    @Override
    public io.objectbox.Property<Record> getIdProperty() {
        return __ID_PROPERTY;
    }

    @Override
    public IdGetter<Record> getIdGetter() {
        return __ID_GETTER;
    }

    @Override
    public CursorFactory<Record> getCursorFactory() {
        return __CURSOR_FACTORY;
    }

    @Internal
    static final class RecordIdGetter implements IdGetter<Record> {
        @Override
        public long getId(Record object) {
            Long id = object.getId();
            return id != null? id : 0;
        }
    }

}
