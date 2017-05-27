package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.BoltNative;
import com.protonail.bolt.jna.impl.Result;

import java.util.EnumSet;
import java.util.function.Consumer;

public class Bolt implements AutoCloseable {
    private long objectId;

    public Bolt(String databaseFileName) {
        this(databaseFileName, BoltFileMode.DEFAULT, null);
    }

    public Bolt(String databaseFileName, EnumSet<BoltFileMode> fileMode) {
        this(databaseFileName, fileMode, null);
    }

    public Bolt(String databaseFileName, EnumSet<BoltFileMode> fileMode, BoltOptions options) {
        long optionsObjectId = options != null ? options.objectId : 0;

        Result.ByValue result = BoltNative.BoltDB_Open(databaseFileName, BoltFileMode.toFlag(fileMode), optionsObjectId);
        result.checkError();

        objectId = result.objectId;
    }

    public void close() {
        BoltNative.BoltDB_Close(objectId);
    }

    public BoltTransaction begin(boolean writeable) {
        Result.ByValue result = BoltNative.BoltDB_Begin(objectId, writeable);
        result.checkError();

        return new BoltTransaction(result.objectId);
    }

    public void update(Consumer<BoltTransaction> callback) {
        BoltTransaction transaction = begin(true);
        try {
            callback.accept(transaction);
            transaction.commit();
        } catch (Throwable ex) {
            transaction.rollback();
            throw ex;
        }
    }

    public void view(Consumer<BoltTransaction> callback) {
        BoltTransaction transaction = begin(false);
        try {
            callback.accept(transaction);
        } finally {
            transaction.rollback();
        }
    }

    public BoltStats getStats() {
        return new BoltStats(BoltNative.BoltDB_Stats(objectId));
    }

    // This is hack to avoid error: could not obtain pthread_keys
    // For example I call it before FileChooser#showSaveDialog because overwise after close dialog the app throw this error.
    public static void init() {
        BoltNative.DoNothing();
    }
}
