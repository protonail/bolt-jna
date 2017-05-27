package com.protonail.bolt.jna.impl;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class BucketStats extends Structure {
    public static class ByValue extends BucketStats implements Structure.ByValue {}

    public int branchPageN;
    public int branchOverflowN;
    public int leafPageN;
    public int leafOverflowN;

    public int keyN;
    public int depth;

    public int branchAlloc;
    public int branchInUse;
    public int leafAlloc;
    public int leafInUse;

    public int bucketN;
    public int inlineBucketN;
    public int inlineBucketInUse;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(
                "branchPageN",
                "branchOverflowN",
                "leafPageN",
                "leafOverflowN",
                "keyN",
                "depth",
                "branchAlloc",
                "branchInUse",
                "leafAlloc",
                "leafInUse",
                "bucketN",
                "inlineBucketN",
                "inlineBucketInUse"
        );
    }
}
