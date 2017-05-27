package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.TransactionStats;

public class BoltTransactionStats {
    private int pageCount;
    private int pageAlloc;

    private int cursorCount;

    private int nodeCount;
    private int nodeDeref;

    private int rebalance;
    private long rebalanceTime;

    private int split;
    private int spill;
    private long spillTime;

    private int write;
    private long writeTime;

    BoltTransactionStats(TransactionStats.ByValue transactionStats) {
        this.pageCount = transactionStats.pageCount;
        this.pageAlloc = transactionStats.pageAlloc;

        this.cursorCount = transactionStats.cursorCount;

        this.nodeCount = transactionStats.nodeCount;
        this.nodeDeref = transactionStats.nodeDeref;

        this.rebalance = transactionStats.rebalance;
        this.rebalanceTime = transactionStats.rebalanceTime;

        this.split = transactionStats.split;
        this.spill = transactionStats.spill;
        this.spillTime = transactionStats.spillTime;

        this.write = transactionStats.write;
        this.writeTime = transactionStats.writeTime;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageAlloc() {
        return pageAlloc;
    }

    public int getCursorCount() {
        return cursorCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getNodeDeref() {
        return nodeDeref;
    }

    public int getRebalance() {
        return rebalance;
    }

    public long getRebalanceTime() {
        return rebalanceTime;
    }

    public int getSplit() {
        return split;
    }

    public int getSpill() {
        return spill;
    }

    public long getSpillTime() {
        return spillTime;
    }

    public int getWrite() {
        return write;
    }

    public long getWriteTime() {
        return writeTime;
    }
}
