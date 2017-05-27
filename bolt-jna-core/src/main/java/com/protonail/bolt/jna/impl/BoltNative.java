package com.protonail.bolt.jna.impl;

import com.sun.jna.*;

public class BoltNative {
    static {
        Native.register("bolt");
    }

    // Common

    public static native void DoNothing();

    public static native void Free(Pointer pointer);

    public static native void Result_Free(Result.ByValue result);

    public static native void Error_Free(Error.ByValue error);

    public static native void Sequence_Free(Sequence.ByValue error);

    // Options

    public static native long BoltDBOptions_Create(long timeout, boolean noGrowSync, boolean readOnly, int mmapFlags, int initialMmapSize);

    public static native void BoltDBOptions_Free(long optionsObjectId);

    // BoltDB

    public static native Result.ByValue BoltDB_Open(String databaseFileName, int fileMode, long optionsObjectId);

    public static native void BoltDB_Close(long boltObjectId);

    public static native Result.ByValue BoltDB_Begin(long boltObjectId, boolean writeable);

    public static native Stats.ByValue BoltDB_Stats(long boltObjectId);

    // Transaction

    public static native Error.ByValue BoltDBTransaction_Commit(long transactionObjectId);

    public static native Error.ByValue BoltDBTransaction_Rollback(long transactionObjectId);

    public static native int BoltDBTransaction_GetId(long transactionObjectId);

    public static native long BoltDBTransaction_Size(long transactionObjectId);

    public static native Result.ByValue BoltDBTransaction_CreateBucket(long transactionObjectId, byte[] name, int nameLength);

    public static native Result.ByValue BoltDBTransaction_CreateBucketIfNotExists(long transactionObjectId, byte[] name, int nameLength);

    public static native Error.ByValue BoltDBTransaction_DeleteBucket(long transactionObjectId, byte[] name, int nameLength);

    public static native Result.ByValue BoltDBTransaction_Bucket(long transactionObjectId, byte[] name, int nameLength);

    public static native long BoltDBTransaction_Cursor(long transactionObjectId);

    public static native TransactionStats.ByValue BoltDBTransaction_Stats(long transactionObjectId);

    // Bucket

    public static native void BoltDBBucket_Free(long bucketObjectId);

    public static native Value.ByValue BoltDBBucket_Get(long bucketObjectId, byte[] key, int keyLength);

    public static native Error.ByValue BoltDBBucket_Put(long bucketObjectId, byte[] key, int keyLength, byte[] value, int valueLength);

    public static native Error.ByValue BoltDBBucket_Delete(long bucketObjectId, byte[] key, int keyLength);

    public static native Result.ByValue BoltDBBucket_CreateBucket(long bucketObjectId, byte[] name, int nameLength);

    public static native Result.ByValue BoltDBBucket_CreateBucketIfNotExists(long bucketObjectId, byte[] name, int nameLength);

    public static native Error.ByValue BoltDBBucket_DeleteBucket(long bucketObjectId, byte[] name, int nameLength);

    public static native Result.ByValue BoltDBBucket_Bucket(long bucketObjectId, byte[] name, int nameLength);

    public static native long BoltDBBucket_Cursor(long bucketObjectId);

    public static native Sequence.ByValue BoltDBBucket_NextSequence(long bucketObjectId);

    public static native BucketStats.ByValue BoltDBBucket_Stats(long bucketObjectId);

    // Cursor

    public static native void BoltDBCursor_Free(long cursorObjectId);

    public static native KeyValue.ByValue BoltDBCursor_First(long cursorObjectId);

    public static native KeyValue.ByValue BoltDBCursor_Last(long cursorObjectId);

    public static native KeyValue.ByValue BoltDBCursor_Next(long cursorObjectId);

    public static native KeyValue.ByValue BoltDBCursor_Prev(long cursorObjectId);

    public static native KeyValue.ByValue BoltDBCursor_Seek(long cursorObjectId, byte[] seek, int seekLength);

    public static native Error.ByValue BoltDBCursor_Delete(long cursorObjectId);
}
