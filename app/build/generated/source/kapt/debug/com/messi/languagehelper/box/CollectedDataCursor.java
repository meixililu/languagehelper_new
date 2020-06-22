package com.messi.languagehelper.box;

import io.objectbox.BoxStore;
import io.objectbox.Cursor;
import io.objectbox.annotation.apihint.Internal;
import io.objectbox.internal.CursorFactory;

// THIS CODE IS GENERATED BY ObjectBox, DO NOT EDIT.

/**
 * ObjectBox generated Cursor implementation for "CollectedData".
 * Note that this is a low-level class: usually you should stick to the Box class.
 */
public final class CollectedDataCursor extends Cursor<CollectedData> {
    @Internal
    static final class Factory implements CursorFactory<CollectedData> {
        @Override
        public Cursor<CollectedData> createCursor(io.objectbox.Transaction tx, long cursorHandle, BoxStore boxStoreForEntities) {
            return new CollectedDataCursor(tx, cursorHandle, boxStoreForEntities);
        }
    }

    private static final CollectedData_.CollectedDataIdGetter ID_GETTER = CollectedData_.__ID_GETTER;


    private final static int __ID_objectId = CollectedData_.objectId.id;
    private final static int __ID_name = CollectedData_.name.id;
    private final static int __ID_json = CollectedData_.json.id;
    private final static int __ID_type = CollectedData_.type.id;

    public CollectedDataCursor(io.objectbox.Transaction tx, long cursor, BoxStore boxStore) {
        super(tx, cursor, CollectedData_.__INSTANCE, boxStore);
    }

    @Override
    public final long getId(CollectedData entity) {
        return ID_GETTER.getId(entity);
    }

    /**
     * Puts an object into its box.
     *
     * @return The ID of the object within its box.
     */
    @Override
    public final long put(CollectedData entity) {
        String objectId = entity.getObjectId();
        int __id1 = objectId != null ? __ID_objectId : 0;
        String name = entity.getName();
        int __id2 = name != null ? __ID_name : 0;
        String json = entity.getJson();
        int __id3 = json != null ? __ID_json : 0;
        String type = entity.getType();
        int __id4 = type != null ? __ID_type : 0;

        long __assignedId = collect400000(cursor, entity.getId(), PUT_FLAG_FIRST | PUT_FLAG_COMPLETE,
                __id1, objectId, __id2, name,
                __id3, json, __id4, type);

        entity.setId(__assignedId);

        return __assignedId;
    }

}
