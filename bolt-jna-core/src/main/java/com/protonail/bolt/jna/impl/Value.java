package com.protonail.bolt.jna.impl;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Value extends Structure {
    public static class ByValue extends Value implements Structure.ByValue, AutoCloseable {
        @Override
        public void close() {
            if (value != null) {
                BoltNative.Free(value);
            }
        }
    }

    public Pointer value;
    public int valueLength;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("value", "valueLength");
    }

    public boolean hasValue() {
        return value != null;
    }

    public byte[] getValue() {
        return value != null ? value.getByteArray(0, valueLength) : null;
    }
}
