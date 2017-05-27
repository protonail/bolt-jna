package com.protonail.bolt.jna.impl;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class Stats extends Structure {
    public static class ByValue extends Stats implements Structure.ByValue {}

    public int freePageN;
    public int pendingPageN;
    public int freeAlloc;
    public int freelistInUse;

    public int txN;
    public int openTxN;
    public TransactionStats.ByValue txStats;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(
                "freePageN",
                "pendingPageN",
                "freeAlloc",
                "freelistInUse",
                "txN",
                "openTxN",
                "txStats"
        );
    }
}
