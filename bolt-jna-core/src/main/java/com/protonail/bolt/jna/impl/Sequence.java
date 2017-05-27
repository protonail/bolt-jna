package com.protonail.bolt.jna.impl;

import com.protonail.bolt.jna.BoltException;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Sequence extends Structure {
    public static class ByValue extends Sequence implements Structure.ByValue {
        public void checkError() {
            if (error != null) {
                BoltNative.Sequence_Free(this);
                throw new BoltException(error);
            }
        }
    }

    public long id;
    public String error;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("id", "error");
    }
}
