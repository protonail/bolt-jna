package com.protonail.bolt.jna.impl;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class KeyValue extends Structure {
    public static class ByValue extends KeyValue implements Structure.ByValue, AutoCloseable {
        @Override
        public void close() {
            if (key != null) {
                BoltNative.Free(key);
            }

            if (value != null) {
                BoltNative.Free(value);
            }
        }
    }

    public Pointer key;
    public int keyLength;
    public Pointer value;
    public int valueLength;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("key", "keyLength", "value", "valueLength");
    }

    public boolean hasKeyValue() {
        return key != null;
    }

    public byte[] getKey() {
        return key != null ? key.getByteArray(0, keyLength) : null;
    }

    public byte[] getValue() {
        return value != null ? value.getByteArray(0, valueLength) : null;
    }
}
