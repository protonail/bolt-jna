package com.protonail.bolt.jna;

import java.util.EnumSet;

public enum BoltFileMode {
    OTHER_EXECUTE,
    OTHER_WRITE,
    OTHER_READ,
    GROUP_EXECUTE,
    GROUP_WRITE,
    GROUP_READ,
    USER_EXECUTE,
    USER_WRITE,
    USER_READ;

    public static final EnumSet<BoltFileMode> DEFAULT = EnumSet.of(USER_READ, USER_WRITE);

    private int value;

    BoltFileMode() {
        this.value = 1 << ordinal();
    }

    public static int toFlag(EnumSet<BoltFileMode> fileModes) {
        int result = 0;
        for (BoltFileMode fileMode : fileModes) {
            result = result | fileMode.value;
        }
        return result;
    }
}
