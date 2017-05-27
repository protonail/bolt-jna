package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.BoltNative;
import com.protonail.bolt.jna.impl.Error;
import com.protonail.bolt.jna.impl.Result;

public class BoltTransaction {
    private long objectId;

    BoltTransaction(long objectId) {
        this.objectId = objectId;
    }

    public void commit() {
        Error.ByValue error = BoltNative.BoltDBTransaction_Commit(objectId);
        error.checkError();
    }

    public void rollback() {
        Error.ByValue error = BoltNative.BoltDBTransaction_Rollback(objectId);
        error.checkError();
    }

    public int getId() {
        return BoltNative.BoltDBTransaction_GetId(objectId);
    }

    public long getDatabaseSize() {
        return BoltNative.BoltDBTransaction_Size(objectId);
    }

    public BoltBucket createBucket(byte[] name) {
        Result.ByValue result = BoltNative.BoltDBTransaction_CreateBucket(objectId, name, name.length);
        result.checkError();

        return new BoltBucket(result.objectId);
    }

    public BoltBucket createBucketIfNotExists(byte[] name) {
        Result.ByValue result = BoltNative.BoltDBTransaction_CreateBucketIfNotExists(objectId, name, name.length);
        result.checkError();

        return new BoltBucket(result.objectId);
    }

    public void deleteBucket(byte[] name) {
        Error.ByValue error = BoltNative.BoltDBTransaction_DeleteBucket(objectId, name, name.length);
        error.checkError();
    }

    public BoltBucket getBucket(byte[] name) {
        Result.ByValue result = BoltNative.BoltDBTransaction_Bucket(objectId, name, name.length);
        result.checkError();

        return new BoltBucket(result.objectId);
    }

    public BoltCursor createCursor() {
        long cursorObjectId = BoltNative.BoltDBTransaction_Cursor(objectId);

        return new BoltCursor(cursorObjectId);
    }

    public BoltTransactionStats getStats() {
        return new BoltTransactionStats(BoltNative.BoltDBTransaction_Stats(objectId));
    }
}
