package com.protonail.bolt.jna.impl;

import com.protonail.bolt.jna.BoltException;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Result extends Structure {
    public static class ByValue extends Result implements Structure.ByValue {
        public void checkError() {
            if (error != null) {
                BoltNative.Result_Free(this);
                throw new BoltException(error);
            }
        }
    }

    public long objectId;
    public String error;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("objectId", "error");
    }
}
