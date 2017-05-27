package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.BoltNative;

public class BoltOptions implements AutoCloseable {
    long objectId;

    public BoltOptions() {
        this(1000);
    }

    public BoltOptions(long timeout) {
        this(timeout, false, false, 0, 0);
    }

    public BoltOptions(long timeout, boolean noGrowSync, boolean readOnly, int mmapFlags, int initialMmapSize) {
        objectId = BoltNative.BoltDBOptions_Create(timeout, noGrowSync, readOnly, mmapFlags, initialMmapSize);
    }

    @Override
    public void close() {
        BoltNative.BoltDBOptions_Free(objectId);
    }
}
