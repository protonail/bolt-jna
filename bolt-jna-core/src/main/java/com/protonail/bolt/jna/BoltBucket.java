package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.*;
import com.protonail.bolt.jna.impl.Error;

public class BoltBucket implements AutoCloseable {
    long objectId;

    BoltBucket(long objectId) {
        this.objectId = objectId;
    }

    @Override
    public void close() {
        BoltNative.BoltDBBucket_Free(objectId);
    }

    public byte[] get(byte[] key) {
        try(Value.ByValue value = BoltNative.BoltDBBucket_Get(objectId, key, key.length)) {
            return value.hasValue() ? value.getValue() : null;
        }
    }

    public void put(byte[] key, byte[] value) {
        Error.ByValue error = BoltNative.BoltDBBucket_Put(objectId, key, key.length, value, value.length);
        error.checkError();
    }

    public void delete(byte[] key) {
        Error.ByValue error = BoltNative.BoltDBBucket_Delete(objectId, key, key.length);
        error.checkError();
    }

    public BoltBucket createBucket(byte[] name) {
        Result.ByValue result = BoltNative.BoltDBBucket_CreateBucket(objectId, name, name.length);
        result.checkError();

        return new BoltBucket(result.objectId);
    }

    public BoltBucket createBucketIfNotExists(byte[] name) {
        Result.ByValue result = BoltNative.BoltDBBucket_CreateBucketIfNotExists(objectId, name, name.length);
        result.checkError();

        return new BoltBucket(result.objectId);
    }

    public void deleteBucket(byte[] name) {
        Error.ByValue error = BoltNative.BoltDBBucket_DeleteBucket(objectId, name, name.length);
        error.checkError();
    }

    public BoltBucket getBucket(byte[] name) {
        Result.ByValue result = BoltNative.BoltDBBucket_Bucket(objectId, name, name.length);
        result.checkError();

        return new BoltBucket(result.objectId);
    }

    public BoltCursor createCursor() {
        long cursorObjectId = BoltNative.BoltDBBucket_Cursor(objectId);

        return new BoltCursor(cursorObjectId);
    }

    public long nextSequenceId() {
        Sequence.ByValue sequence = BoltNative.BoltDBBucket_NextSequence(objectId);
        sequence.checkError();

        return sequence.id;
    }

    public BoltBucketStats getStats() {
        return new BoltBucketStats(BoltNative.BoltDBBucket_Stats(objectId));
    }
}
