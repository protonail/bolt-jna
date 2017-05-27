package com.protonail.bolt.jna.impl;

import com.protonail.bolt.jna.BoltException;
import com.sun.jna.Structure;

import java.util.Collections;
import java.util.List;

public class Error extends Structure {
    public static class ByValue extends Error implements Structure.ByValue {
        public void checkError() {
            if (error != null) {
                BoltNative.Error_Free(this);
                throw new BoltException(error);
            }
        }
    }

    public String error;

    @Override
    protected List<String> getFieldOrder() {
        return Collections.singletonList("error");
    }
}
