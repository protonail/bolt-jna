package com.protonail.bolt.jna;

public class BoltKeyValue {
    private byte[] key;
    private byte[] value;

    public BoltKeyValue(byte[] key, byte[] value) {
        this.key = key;
        this.value = value;
    }

    public boolean isKey() {
        return key != null && value != null;
    }

    public boolean isBucket() {
        return key != null && value == null;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getValue() {
        return value;
    }
}
