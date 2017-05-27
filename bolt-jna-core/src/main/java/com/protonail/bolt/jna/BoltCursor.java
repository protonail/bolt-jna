package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.BoltNative;
import com.protonail.bolt.jna.impl.Error;
import com.protonail.bolt.jna.impl.KeyValue;

public class BoltCursor implements AutoCloseable {
    long objectId;

    public BoltCursor(long objectId) {
        this.objectId = objectId;
    }

    @Override
    public void close() {
        BoltNative.BoltDBCursor_Free(objectId);
    }

    public BoltKeyValue first() {
        try(KeyValue.ByValue keyValue = BoltNative.BoltDBCursor_First(objectId)) {
            return keyValue.hasKeyValue() ? new BoltKeyValue(keyValue.getKey(), keyValue.getValue()) : null;
        }
    }

    public BoltKeyValue last() {
        try(KeyValue.ByValue keyValue = BoltNative.BoltDBCursor_Last(objectId)) {
            return keyValue.hasKeyValue() ? new BoltKeyValue(keyValue.getKey(), keyValue.getValue()) : null;
        }
    }

    public BoltKeyValue next() {
        try(KeyValue.ByValue keyValue = BoltNative.BoltDBCursor_Next(objectId)) {
            return keyValue.hasKeyValue() ? new BoltKeyValue(keyValue.getKey(), keyValue.getValue()) : null;
        }
    }

    public BoltKeyValue prev() {
        try(KeyValue.ByValue keyValue = BoltNative.BoltDBCursor_Prev(objectId)) {
            return keyValue.hasKeyValue() ? new BoltKeyValue(keyValue.getKey(), keyValue.getValue()) : null;
        }
    }

    public BoltKeyValue seek(byte[] seek) {
        try(KeyValue.ByValue keyValue = BoltNative.BoltDBCursor_Seek(objectId, seek, seek.length)) {
            return keyValue.hasKeyValue() ? new BoltKeyValue(keyValue.getKey(), keyValue.getValue()) : null;
        }
    }

    public void delete() {
        Error.ByValue error = BoltNative.BoltDBCursor_Delete(objectId);
        error.checkError();
    }
}
