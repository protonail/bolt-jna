package com.protonail.bolt.jna;

import com.protonail.bolt.jna.impl.BucketStats;

public class BoltBucketStats {
    private int branchPageN;
    private int branchOverflowN;
    private int leafPageN;
    private int leafOverflowN;

    private int keyN;
    private int depth;

    private int branchAlloc;
    private int branchInUse;
    private int leafAlloc;
    private int leafInUse;

    private int bucketN;
    private int inlineBucketN;
    private int inlineBucketInUse;

    BoltBucketStats(BucketStats.ByValue bucketStats) {
        this.branchPageN = bucketStats.branchPageN;
        this.branchOverflowN = bucketStats.branchOverflowN;
        this.leafPageN = bucketStats.leafPageN;
        this.leafOverflowN = bucketStats.leafOverflowN;

        this.keyN = bucketStats.keyN;
        this.depth = bucketStats.depth;

        this.branchAlloc = bucketStats.branchAlloc;
        this.branchInUse = bucketStats.branchInUse;
        this.leafAlloc = bucketStats.leafAlloc;
        this.leafInUse = bucketStats.leafInUse;

        this.bucketN = bucketStats.bucketN;
        this.inlineBucketN = bucketStats.inlineBucketN;
        this.inlineBucketInUse = bucketStats.inlineBucketInUse;
    }

    public int getBranchPageN() {
        return branchPageN;
    }

    public int getBranchOverflowN() {
        return branchOverflowN;
    }

    public int getLeafPageN() {
        return leafPageN;
    }

    public int getLeafOverflowN() {
        return leafOverflowN;
    }

    public int getKeyN() {
        return keyN;
    }

    public int getDepth() {
        return depth;
    }

    public int getBranchAlloc() {
        return branchAlloc;
    }

    public int getBranchInUse() {
        return branchInUse;
    }

    public int getLeafAlloc() {
        return leafAlloc;
    }

    public int getLeafInUse() {
        return leafInUse;
    }

    public int getBucketN() {
        return bucketN;
    }

    public int getInlineBucketN() {
        return inlineBucketN;
    }

    public int getInlineBucketInUse() {
        return inlineBucketInUse;
    }
}
