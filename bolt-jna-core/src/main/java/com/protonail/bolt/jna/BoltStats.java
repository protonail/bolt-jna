package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.Stats;

public class BoltStats {
    private int freePageN;
    private int pendingPageN;
    private int freeAlloc;
    private int freelistInUse;

    private int transactionN;
    private int openTransactionN;
    private BoltTransactionStats transactionStats;

    public BoltStats(Stats.ByValue stats) {
        this.freePageN = stats.freePageN;
        this.pendingPageN = stats.pendingPageN;
        this.freeAlloc = stats.freeAlloc;
        this.freelistInUse = stats.freelistInUse;

        this.transactionN = stats.txN;
        this.openTransactionN = stats.openTxN;
        this.transactionStats = new BoltTransactionStats(stats.txStats);
    }

    public int getFreePageN() {
        return freePageN;
    }

    public int getPendingPageN() {
        return pendingPageN;
    }

    public int getFreeAlloc() {
        return freeAlloc;
    }

    public int getFreelistInUse() {
        return freelistInUse;
    }

    public int getTransactionN() {
        return transactionN;
    }

    public int getOpenTransactionN() {
        return openTransactionN;
    }

    public BoltTransactionStats getTransactionStats() {
        return transactionStats;
    }
}
