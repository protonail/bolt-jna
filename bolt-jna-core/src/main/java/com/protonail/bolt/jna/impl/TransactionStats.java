package com.protonail.bolt.jna.impl;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class TransactionStats extends Structure {
    public static class ByValue extends TransactionStats implements Structure.ByValue {}

    public int pageCount;
    public int pageAlloc;

    public int cursorCount;

    public int nodeCount;
    public int nodeDeref;

    public int rebalance;
    public long rebalanceTime;

    public int split;
    public int spill;
    public long spillTime;

    public int write;
    public long writeTime;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(
                "pageCount",
                "pageAlloc",
                "cursorCount",
                "nodeCount",
                "nodeDeref",
                "rebalance",
                "rebalanceTime",
                "split",
                "spill",
                "spillTime",
                "write",
                "writeTime");
    }
}
